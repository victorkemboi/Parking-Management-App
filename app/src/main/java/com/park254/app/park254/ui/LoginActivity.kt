package com.park254.app.park254.ui
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.park254.app.park254.R
import com.park254.app.park254.ui.repo.LoginViewModel
import javax.inject.Inject
import android.content.Intent
import kotlinx.android.synthetic.main.activity_login.*
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.api.ApiException
import android.support.v4.app.FragmentActivity
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.FacebookCallback
import com.facebook.AccessToken
import com.facebook.AccessTokenTracker
import com.facebook.login.LoginManager
import com.park254.app.park254.App
import com.park254.app.park254.di.AppModule
import dagger.android.AndroidInjection
import dagger.android.support.DaggerAppCompatActivity
import java.util.*

class LoginActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
             //   App.INSTANCE.daggerAppComponent.inject(this)
        //AndroidInjection.inject(this)

        App.applicationInjector.inject(this)

        google_sign_in_button.setOnClickListener { googleSignIn() }

       // LoginManager.getInstance().setReadPermissions(viewModel.EMAIL)

      facebook_sign_in_button.setOnClickListener { view->

          Log.d("Fb Button","PRESSED")
          LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList(viewModel.EMAIL));
      }

        LoginManager.getInstance().registerCallback(viewModel.callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                // App code

                Log.d("Fb Login","SUCCESS")

                viewModel.handleFacebookAccessToken(loginResult.accessToken);
            }

            override fun onCancel() {
                // App code
                Log.w("Fb Login","cancelled")
            }

            override fun onError(exception: FacebookException) {
                // App code

                Log.e("Fb Login","FAILED")

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
                viewModel.firebaseAuthwithGoogle(account)

                //check if new user
                //if new user, ask for more info
                //if existing user, differentiate according to roles.
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("Sign In", "Google sign in failed", e)
                //display error message, and allow to re-sign in again.
            }

        }else{
            viewModel.callbackManager.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun googleSignIn() {
        viewModel.setupGoogleUserData(this.application )
        val signInIntent = viewModel.mGoogleSignInClient.getSignInIntent()
        startActivityForResult(signInIntent,  viewModel.RC_GOOGLE_SIGN_IN)
    }


}
