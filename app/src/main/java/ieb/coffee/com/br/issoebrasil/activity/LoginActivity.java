package ieb.coffee.com.br.issoebrasil.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import ieb.coffee.com.br.issoebrasil.R;
import ieb.coffee.com.br.issoebrasil.config.ConfiguracaoFirebase;
import ieb.coffee.com.br.issoebrasil.helper.Base64Custom;
import ieb.coffee.com.br.issoebrasil.helper.Preferencias;
import ieb.coffee.com.br.issoebrasil.model.Conversa;
import ieb.coffee.com.br.issoebrasil.model.Mensagem;
import ieb.coffee.com.br.issoebrasil.model.Usuario;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;


public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText senha;
    private Button botaoLogar;
    private Usuario usuario;
    private ValueEventListener valueEventListenerUsuario;
    private DatabaseReference firebase;
    private String identificadorUsuarioLogado;
    private FirebaseAuth autenticacao;
    private CallbackManager mCallbackManager;
    private LoginButton loginButton;

    //Dados do destinatario
    private String nomeUsuarioDestinatario;
    private String idUsuarioDestinatario;

    //Dados do remetente
    private String idUsuarioRemetente;
    private String nomeUsuarioRemetente;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = autenticacao.getCurrentUser();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        verificarUsuarioLogado();

        usuario = new Usuario();

        email = (EditText) findViewById(R.id.edit_login_email);
        senha = (EditText) findViewById(R.id.edit_login_senha);
        botaoLogar = (Button) findViewById(R.id.bt_login);

        botaoLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usuario = new Usuario();
                usuario.setEmail(email.getText().toString());
                usuario.setSenha(senha.getText().toString());
                validarLogin();
            }
        });

        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.bt_login_facebook);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
                public void onSuccess(LoginResult loginResult) {
                Log.d("LOG", "facebook:onSuccess:" + loginResult.getAccessToken());
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("CANCEL", "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("ERRO", "facebook:onError", error);
                // ...
            }
        });
        autenticacao = FirebaseAuth.getInstance();


    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("TAG", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        autenticacao.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            final FirebaseUser user = autenticacao.getCurrentUser();
                            identificadorUsuarioLogado = Base64Custom.codificarBase64(user.getEmail());
                            cadastrarUsuario(user);
                            firebase = ConfiguracaoFirebase.getFirebase()
                                    .child("usuarios")
                                    .child(identificadorUsuarioLogado);

                            valueEventListenerUsuario = new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Usuario usuarioRecuperado = dataSnapshot.getValue(Usuario.class);
                                    Preferencias preferencias = new Preferencias(LoginActivity.this);
                                    preferencias.salvarUsuarioPreferencias(identificadorUsuarioLogado, user.getDisplayName());
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            };

                            firebase.addListenerForSingleValueEvent(valueEventListenerUsuario);


                            abrirTelaPrincipal();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void cadastrarUsuario(final FirebaseUser user) {
        Toast.makeText(LoginActivity.this, "Sucesso ao cadastrar usuário", Toast.LENGTH_LONG).show();
        String identificadorUsuario = Base64Custom.codificarBase64(user.getEmail());
        usuario.setId(identificadorUsuario);
        usuario.setEmail(user.getEmail());
        usuario.setNome(user.getDisplayName());
        usuario.setImg(user.getPhotoUrl().toString());
        usuario.setTelefone(user.getPhoneNumber());
        usuario.salvar();

        nomeUsuarioRemetente = user.getDisplayName();
        nomeUsuarioDestinatario = "Matheus Freitas";
        idUsuarioDestinatario = Base64Custom.codificarBase64("matheus@gmail.com");




        Preferencias preferencias = new Preferencias(LoginActivity.this);
        preferencias.salvarUsuarioPreferencias(identificadorUsuario, usuario.getNome());

        Mensagem mensagem = new Mensagem();
        mensagem.setIdUsuario(idUsuarioRemetente);
        mensagem.setMensagem("Olá, Seja Bem Vindo!");

        // salvamos mensagem para o remetente
        Boolean retornoMensagemRemetente = salvarMensagem(identificadorUsuarioLogado, idUsuarioDestinatario, mensagem);
        if(!retornoMensagemRemetente){
            Toast.makeText(LoginActivity.this, "Problema ao salvar mensagem, tente novamente", Toast.LENGTH_LONG).show();
        }else{

            // salvamos mensagem para o destinatario
            Boolean retornoMensagemDestinatario = salvarMensagem(idUsuarioDestinatario,idUsuarioRemetente, mensagem);
            if(!retornoMensagemRemetente){
                Toast.makeText(LoginActivity.this, "Problema ao salvar mensagem do destinatário, tente novamente!", Toast.LENGTH_LONG).show();
            }
        }

        // salvamos Conversa para o remetente
        Conversa conversa = new Conversa();
        conversa.setIdUsuario(idUsuarioDestinatario);
        conversa.setNome(nomeUsuarioDestinatario);
        conversa.setMensagem("Olá, Seja Bem Vindo!");
        Boolean retornoConversaRemetente = salvarConversa(idUsuarioRemetente, idUsuarioDestinatario, conversa);
        if(!retornoConversaRemetente){
            Toast.makeText(LoginActivity.this, "Problema ao salvar conversa, tente novamente!", Toast.LENGTH_LONG).show();
        }else{
            // salvamos Conversa para o destinatario
            conversa = new Conversa();
            conversa.setIdUsuario(idUsuarioRemetente);
            conversa.setNome(nomeUsuarioRemetente);
            conversa.setMensagem("Olá, Seja Bem Vindo!");
            salvarConversa(idUsuarioDestinatario, idUsuarioRemetente, conversa);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void validarLogin(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    identificadorUsuarioLogado = Base64Custom.codificarBase64(usuario.getEmail());

                    firebase = ConfiguracaoFirebase.getFirebase()
                            .child("usuarios")
                            .child(identificadorUsuarioLogado);

                    valueEventListenerUsuario = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Usuario usuarioRecuperado = dataSnapshot.getValue(Usuario.class);
                            Preferencias preferencias = new Preferencias(LoginActivity.this);
                            preferencias.salvarUsuarioPreferencias(identificadorUsuarioLogado, usuario.getNome());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    };

                    firebase.addListenerForSingleValueEvent(valueEventListenerUsuario);


                    abrirTelaPrincipal();
                    Toast.makeText(LoginActivity.this, "Sucesso ao fazer login!", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(LoginActivity.this, "Erro ao fazer login!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean salvarConversa(String idRemetente, String idDestinatario, Conversa conversa){
        try{
            firebase = ConfiguracaoFirebase.getFirebase().child("conversas");
            firebase.child(idRemetente)
                    .child(idDestinatario)
                    .setValue(conversa);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    private boolean salvarMensagem(String idRemetente, String idDestinatario, Mensagem mensagem){
        try {

            firebase = ConfiguracaoFirebase.getFirebase().child("mensagens");
            firebase.child(idRemetente)
                    .child(idDestinatario)
                    .push().setValue(mensagem);


            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    private void verificarUsuarioLogado(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        if(autenticacao.getCurrentUser() != null){
            abrirTelaPrincipal();
        }
    }

    private void abrirTelaPrincipal() {
        Intent intent = new Intent(LoginActivity.this, DrawerActivity.class);
        startActivity(intent);
        finish();
    }



    public void abrirCadastroUsuario(View view){
        Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
        startActivity(intent);
    }
}
