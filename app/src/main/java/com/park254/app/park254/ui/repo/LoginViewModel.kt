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
constructor( val firebaseAuth:FirebaseAuth) : ViewModel() {
      lateinit var gso : GoogleSignInOptions
      lateinit var  mGoogleSignInClient : GoogleSignInClient
      val RC_GOOGLE_SIGN_IN = 1
      val RC_FACEBOOK_SIGN_IN = 2
      val GOOGLE_WEB_CLIENT_ID = "532535428473-4v6vfn6ijcj19n4q5q2aau53auflh2g4.apps.googleusercontent.com"

      val callbackManager = CallbackManager.Factory.create()
      var mFacebookAccessTokenTracker: AccessTokenTracker? = null
      val EMAIL = "email"
     var phoneNumber = ""
     var gender = 0


     fun setupGoogleUserData( app: Application){
         //requestGoogleUserData
         gso  = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                 .requestIdToken(GOOGLE_WEB_CLIENT_ID)
                 .requestEmail()
                 .build()
         //buildAGoogleSignInClient
         mGoogleSignInClient = GoogleSignIn.getClient(app.applicationContext, gso);
     }


}