package com.park254.app.park254.ui
import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.park254.app.park254.R
import com.park254.app.park254.ui.repo.LoginViewModel
import javax.inject.Inject
import android.content.Intent
import android.support.annotation.NonNull
import kotlinx.android.synthetic.main.activity_login.*
import com.google.android.gms.common.api.ApiException
import android.util.Log
import android.view.View
import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.FacebookCallback
import com.facebook.login.LoginManager
import com.google.firebase.auth.FacebookAuthProvider
import com.park254.app.park254.App
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GetTokenResult
import com.google.firebase.auth.GoogleAuthProvider
import com.park254.app.park254.models.Settings
import com.park254.app.park254.models.User
import com.park254.app.park254.network.RetrofitApiService
import com.park254.app.park254.utils.SharedPrefs
import com.park254.app.park254.utils.livedata_adapter.ApiResponse
import java.util.*

class LoginActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModel: LoginViewModel

    @Inject
    lateinit var settings: SharedPrefs

    @Inject
    lateinit var retrofitApiService: RetrofitApiService



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        (application as App).applicationInjector.inject(this)

       google_sign_in_button.setOnClickListener {
           lyt_login_btn.visibility = View.GONE
           lyt_progress_login.visibility = View.VISIBLE
           googleSignIn()
       }


      facebook_sign_in_button.setOnClickListener { view->

          //Log.d("Fb Button","PRESSED")
          lyt_login_btn.visibility = View.GONE
          lyt_progress_login.visibility = View.VISIBLE
          LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList(viewModel.EMAIL))
      }

        LoginManager.getInstance().registerCallback(viewModel.callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                // App code

               // Log.d("Fb Login","SUCCESS")

                handleFacebookAccessToken(loginResult.accessToken)
            }

            override fun onCancel() {
                // App code
               // Log.w("Fb Login","cancelled")

                lyt_login_btn.visibility = View.VISIBLE
                lyt_progress_login.visibility = View.GONE
            }

            override fun onError(exception: FacebookException) {
                // App code

                //Log.e("Fb Login","FAILED")
                lyt_login_btn.visibility = View.VISIBLE
                lyt_progress_login.visibility = View.GONE

            }
        })

    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == viewModel.RC_GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
               firebaseAuthwithGoogle(account)

                //check if new user
                //if new user, ask for more info
                //if existing user, differentiate according to roles.
            } catch (e: ApiException) {
                lyt_login_btn.visibility = View.VISIBLE
                lyt_progress_login.visibility = View.GONE
                // Google Sign In failed, update UI appropriately
                //Log.w("Sign In", "Google sign in failed", e)
                //display error message, and allow to re-sign in again.
            }

        }else{
            viewModel.callbackManager.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun googleSignIn() {
        viewModel.setupGoogleUserData(this.application )
        val signInIntent = viewModel.mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent,  viewModel.RC_GOOGLE_SIGN_IN)
    }

    fun handleFacebookAccessToken(token: AccessToken) {



        val credential = FacebookAuthProvider.getCredential(token.token)
        viewModel.firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener { task->
                    if(task.isSuccessful){
                        viewModel.firebaseAuth.getAccessToken(true).addOnCompleteListener {
                            task2 ->
                            run {
                                if (task2.isSuccessful) {
                                    val userToken = task2.result.token
                                    settings.token = userToken


                                    if( task.result.additionalUserInfo.isNewUser){

                                        startActivity(
                                                Intent(this@LoginActivity, AddUserInfoActivity::class.java))
                                    }else{
                                        saveUserId()
                                    }


                                }
                            }
                        }

                    }else{
                        //Log.w("Sign In", "signInWithCredential:failed")
                    }
                }
    }

    private fun firebaseAuthwithGoogle(account: GoogleSignInAccount){

      //  Log.d("Sign In", "firebaseAuthWithGoogle:" + account.id)
        //progressdialogstart
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)

       // Log.d("Sign In", "firebaseAuthWithGoogle: google auth provider passed")
        viewModel.firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener { task->
                  //  Log.w("Sign In task", task.toString())
                    if(task.isSuccessful){


                        viewModel.firebaseAuth.getAccessToken(true).addOnCompleteListener {
                            task2 ->
                            run {
                                if (task2.isSuccessful) {
                                    val token = task2.result.token
                                    settings.token = token

                                   // Log.d("Sign In jwt token", token   )

                                    if( task.result.additionalUserInfo.isNewUser){

                                        startActivity(
                                                Intent(this@LoginActivity, AddUserInfoActivity::class.java))


                                    }else{
                                        saveUserId()
                                    }


                                }
                            }
                        }
                    }else{
                       // Log.w("Sign In", "signInWithCredential:failed")

                        lyt_login_btn.visibility = View.VISIBLE
                        lyt_progress_login.visibility = View.GONE
                    }
                }
    }

    private fun saveUserId(){
        retrofitApiService.registerUser().observe(this, Observer<ApiResponse<User>> {
            response->
            if (response?.body != null) {
                val user = response.body

                settings.userId = user.id
                if (user.phoneNumber != ""){
                    startActivity(
                            Intent(this@LoginActivity, HomeActivity::class.java))
                    finish()
                }else{
                    startActivity(
                            Intent(this@LoginActivity, AddUserInfoActivity::class.java))
                    finish()
                }


            }


        })
    }

    override fun onBackPressed() {
        finish()
    }
}
