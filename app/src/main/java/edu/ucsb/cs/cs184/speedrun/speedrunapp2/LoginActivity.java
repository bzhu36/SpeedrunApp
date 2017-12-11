package edu.ucsb.cs.cs184.speedrun.speedrunapp2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "GoogleActivity";
    private FirebaseAuth mAuth;
    public static GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN=123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("103099140352-se5ivcskftfg1284a4g647a6b4pbfl7j.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mAuth = FirebaseAuth.getInstance();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        SignInButton signInButton=findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(this);
        Button withoutLogButton=findViewById(R.id.button2);
        withoutLogButton.setOnClickListener(this);
    }

    @Override
    protected void onStart(){
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(currentUser);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.button2:
                Intent intent=new Intent(this, MainActivity.class);
                startActivity(intent);
                break;

        }
    }

    private void signIn() {
        Intent signInIntent=mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("result code = " +resultCode);
        if(requestCode==RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                //handleSignInResult(result);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e){
                Log.w(TAG, "Google sign in failed", e);
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }


        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
                            ref.setValue(new UserDatabase(user.getEmail(), user.getDisplayName()));
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            updateUI(null);
                        }
                    }
                });
    }

//    private void handleSignInResult(GoogleSignInResult result) {
//        if(result.isSuccess()){
//            //The Google Account:
//            GoogleSignInAccount account=result.getSignInAccount();
//            //Can retrieve aspects from it like name and email:
//            //String name=account.getDisplayName();
//            //String email=account.getEmail();
//            updateUI(account);
//
//        }
//        else{
//            updateUI(null);
//        }
//    }

    private void updateUI(FirebaseUser account){
        if(account!=null){
            Intent intent=new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}
