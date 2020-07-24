package com.example.sharinglocation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
EditText userEmail,userPassowrd;
Button loginButton,signUpButton;
FirebaseAuth mAuth;
String email,password;
RadioButton yesRadioButton,noRadioButton;
RadioGroup isShopOwnerRadioGroup;
public static String isShopOwner;

    public String getIsShopOwner() {
        return isShopOwner;
    }

    FirebaseUser user;
FirebaseDatabase database;
DatabaseReference reference;

    public void goToMap(){
        Intent gotoMapActivity= new Intent(MainActivity.this,MapsActivity.class).putExtra("isShopOwner",isShopOwner);
        startActivity(gotoMapActivity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitializeFields();
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               LoginUser();
                                           }
                                       }
        );
        isShopOwnerRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selected_RadioButton=(RadioButton)findViewById(checkedId);
                if(selected_RadioButton.getText().toString().equals("Yes")){
                    isShopOwner="Yes";
                }
                else
                    isShopOwner="No";
            }
        });

    }

    private void LoginUser() {
        email=userEmail.getText().toString();
        password=userPassowrd.getText().toString();
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)&&(isShopOwnerRadioGroup.isPressed()))
        Toast.makeText(this, "Please fill all the details ", Toast.LENGTH_SHORT).show();
    else {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "created succesfully", Toast.LENGTH_SHORT).show();
                    //updateDatabase();
                    goToMap();
                } else {
                    String message = task.getException().toString();
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();

                }

            }
        });

    }
    }

    private void updateDatabase() {
        if(user!=null){
            reference.child("Location").child(user.getUid()).child("isShop").setValue(isShopOwner);
        }
        else
            updateDatabase();
    }

    private void InitializeFields() {
        userEmail=(EditText)findViewById(R.id.emailEditTextSec);
        userPassowrd=(EditText)findViewById(R.id.PasswordEditText);
        loginButton=(Button)findViewById(R.id.loginButton);
        signUpButton=(Button)findViewById(R.id.signUpButton);
        mAuth=FirebaseAuth.getInstance();
        yesRadioButton=(RadioButton)findViewById(R.id.yesRadioButton);
        noRadioButton=(RadioButton)findViewById(R.id.noRadioButton);
        isShopOwnerRadioGroup=(RadioGroup)findViewById(R.id.isShopOwnerRadioGroup);
        user=FirebaseAuth.getInstance().getCurrentUser();
        database=FirebaseDatabase.getInstance();
        reference=database.getReference();
    }


    private void createAccount() {
        email=userEmail.getText().toString();
        password=userPassowrd.getText().toString();
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)&&(isShopOwnerRadioGroup.isPressed()))
            Toast.makeText(this, "Please fill all the details ", Toast.LENGTH_SHORT).show();
        else {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "created succesfully", Toast.LENGTH_SHORT).show();
                        //updateDatabase();
                        goToMap();
                    } else {
                        String message = task.getException().toString();
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();

                    }

                }
            });

        }
    }
}
