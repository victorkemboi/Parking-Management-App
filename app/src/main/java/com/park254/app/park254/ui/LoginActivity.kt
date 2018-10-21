package com.park254.app.park254.ui
import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.facebook.AccessToken
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.GoogleAuthProvider
import com.park254.app.park254.App
import com.park254.app.park254.R
import com.park254.app.park254.models.User
import com.park254.app.park254.network.RetrofitApiService
import com.park254.app.park254.ui.repo.LoginViewModel
import com.park254.app.park254.utils.SharedPrefs
import com.park254.app.park254.utils.UtilityClass
import com.park254.app.park254.utils.livedata_adapter.ApiResponse
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*
import javax.inject.Inject

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

        viewModel.networkStateSnackBar = Snackbar.make(window.decorView.rootView,
                "Unavailable internet connection!", Snackbar.LENGTH_INDEFINITE).withColor(
                ContextCompat.getColor(this,R.color.red_600) )

       google_sign_in_button.setOnClickListener {
           Log.w("G Button:","PRESSED")

           if (UtilityClass.isOnline(this)){
               viewModel.networkStateSnackBar?.dismiss()
               lyt_login_btn.visibility = View.GONE
               lyt_progress_login.visibility = View.VISIBLE
               googleSignIn()
           }else{
                 viewModel.networkStateSnackBar?.show()
           }

       }

      facebook_sign_in_button.setOnClickListener { view->

          //Log.d("Fb Button","PRESSED")
          if (UtilityClass.isOnline(this)) {
              viewModel.networkStateSnackBar?.dismiss()
              lyt_login_btn.visibility = View.GONE
              lyt_progress_login.visibility = View.VISIBLE
              LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList(viewModel.EMAIL))

          }else{
              viewModel.networkStateSnackBar?.show()
          }
      }

        LoginManager.getInstance().registerCallback(viewModel.callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Log.w("Fb callback:","success")
                handleFacebookAccessToken(loginResult.accessToken)
            }

            override fun onCancel() {
                // App code
               // Log.w("Fb Login","cancelled")
                Log.w("Fb callback:","cancelled")
                lyt_login_btn.visibility = View.VISIBLE
                lyt_progress_login.visibility = View.GONE
            }

            override fun onError(exception: FacebookException) {
                // App code

                Log.w("Fb callback:","failed")
                lyt_login_btn.visibility = View.VISIBLE
                lyt_progress_login.visibility = View.GONE

            }
        })

    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == viewModel.RC_GOOGLE_SIGN_IN ) {
            Log.w("G Button:","onActivityResult google sign in")
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

          try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    Log.w("G Button:","onActivityResult google sign in success")
                    firebaseAuthwithGoogle(account)
                }

            } catch (e: ApiException) {
                lyt_login_btn.visibility = View.VISIBLE
                lyt_progress_login.visibility = View.GONE

                Log.w("G Button:statusCode=","${e.statusCode}")
                Log.w("G Button:message=","${e.message}")
                Log.w("G Button:cause=","${e.cause}")
            }


        }else{
            viewModel.callbackManager.onActivityResult(requestCode, resultCode, data)
            Log.w("Sign In", "Google sign in failed")
        }
    }

    private fun googleSignIn() {
        Log.w("G Button:","googleSignIn()")

        viewModel.setupGoogleUserData(this)
        startActivityForResult(viewModel.mGoogleSignInClient.signInIntent,  viewModel.RC_GOOGLE_SIGN_IN)
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
                                    val userToken = task2.result?.token
                                    settings.token = userToken

                                    if( task.result?.additionalUserInfo!!.isNewUser){

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

        Log.w("G Button:","firebaseAuthwithGoogle(account: GoogleSignInAccount)")
      //  Log.d("Sign In", "firebaseAuthWithGoogle:" + account.id)
        //progressdialogstart
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)

       // Log.d("Sign In", "firebaseAuthWithGoogle: google auth provider passed")
        viewModel.firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener { task->
                  //  Log.w("Sign In task", task.toString())
                    if(task.isSuccessful){
                        Log.w("G Button:","signInWithCredential(credential) success")
                        viewModel.firebaseAuth.getAccessToken(true).addOnCompleteListener {
                            task2 ->
                            run {
                                if (task2.isSuccessful) {
                                    val token = task2.result?.token
                                    settings.token = token

                                   // Log.d("Sign In jwt token", token   )

                                    if( task.result?.additionalUserInfo!!.isNewUser){

                                        startActivity(
                                                Intent(this@LoginActivity, AddUserInfoActivity::class.java))

                                    }else{
                                        saveUserId()
                                    }

                                }
                            }
                        }
                    }else{
                        Log.w("G Button:","signInWithCredential(credential) failed")
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

    private fun Snackbar.withColor(colorInt: Int): Snackbar {
        this.view.setBackgroundColor(colorInt)
        return this
    }
}
