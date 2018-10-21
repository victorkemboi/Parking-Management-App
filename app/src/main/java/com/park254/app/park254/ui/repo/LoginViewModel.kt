package com.park254.app.park254.ui.repo

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.content.Context
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
import android.support.design.widget.Snackbar
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.AuthCredential
import com.facebook.AccessToken
import com.park254.app.park254.App
import com.park254.app.park254.R


class LoginViewModel @Inject
constructor( val firebaseAuth:FirebaseAuth) : ViewModel() {

      lateinit var gso : GoogleSignInOptions
      lateinit var  mGoogleSignInClient : GoogleSignInClient
      val RC_GOOGLE_SIGN_IN = 1
      val RC_FACEBOOK_SIGN_IN = 2
      //val GOOGLE_WEB_CLIENT_ID = "532535428473-4v6vfn6ijcj19n4q5q2aau53auflh2g4.apps.googleusercontent.com"
      val GOOGLE_WEB_CLIENT_ID = "532535428473-4v6vfn6ijcj19n4q5q2aau53auflh2g4.apps.googleusercontent.com"
      val callbackManager = CallbackManager.Factory.create()
      var mFacebookAccessTokenTracker: AccessTokenTracker? = null
      val EMAIL = "email"
     var phoneNumber = ""
     var gender = 0

     var networkStateSnackBar:Snackbar? = null


     fun setupGoogleUserData( context:Context){
         //requestGoogleUserData
         Log.w("G Button:","setupGoogleUserData( app: Application)")
         gso  = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                 .requestIdToken(context.getString(R.string.default_web_client_id))
                 .requestEmail()
                 .build()
         //buildAGoogleSignInClient
         mGoogleSignInClient = GoogleSignIn.getClient(context, gso)
     }


}