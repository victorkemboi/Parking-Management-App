package com.park254.app.park254.ui.repo

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import javax.inject.Inject
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.facebook.AccessTokenTracker
import android.widget.Toast
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.AuthResult
import android.support.annotation.NonNull
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.AuthCredential
import com.facebook.AccessToken
import com.park254.app.park254.App


class LoginViewModel @Inject
constructor(private val firebaseAuth:FirebaseAuth) : ViewModel() {
      lateinit var gso : GoogleSignInOptions
      lateinit var  mGoogleSignInClient : GoogleSignInClient
      val RC_GOOGLE_SIGN_IN = 1
      val RC_FACEBOOK_SIGN_IN = 2
      val GOOGLE_WEB_CLIENT_ID = "532535428473-4v6vfn6ijcj19n4q5q2aau53auflh2g4.apps.googleusercontent.com"

      val callbackManager = CallbackManager.Factory.create()
      var mFacebookAccessTokenTracker: AccessTokenTracker? = null
      val EMAIL = "email"
     fun setupGoogleUserData( app: Application){
         //requestGoogleUserData
         gso  = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                 .requestIdToken(GOOGLE_WEB_CLIENT_ID)
                 .requestEmail()
                 .build()
         //buildAGoogleSignInClient
         mGoogleSignInClient = GoogleSignIn.getClient(app.applicationContext, gso);
     }

    fun firebaseAuthwithGoogle(account:GoogleSignInAccount):Boolean{

        var requestState: Boolean = false
        Log.d("Sign In", "firebaseAuthWithGoogle:" + account.id)
        //progressdialogstart
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)

        Log.d("Sign In", "firebaseAuthWithGoogle: google auth provider passed")
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener { task->
                    Log.w("Sign In task", task.toString())
                    if(task.isSuccessful){
                        Log.d("Sign In", "signInWithCredential:success")
                      var  user = firebaseAuth.currentUser
                        requestState = true

                    }else{
                        Log.w("Sign In", "signInWithCredential:failed")
                    }
                }

       return requestState
    }

     fun handleFacebookAccessToken(token: AccessToken) : Boolean {
        var requestState : Boolean = false
        Log.d("FBTokenAccess", "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener { task->
                    if(task.isSuccessful){
                        Log.d("Sign In", "signInWithCredential:success")
                        var  user = firebaseAuth.currentUser
                        requestState = true

                    }else{
                        Log.w("Sign In", "signInWithCredential:failed")
                    }
                }

        return requestState;
    }




}