package ieb.coffee.com.br.issoebrasil.adpter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import ieb.coffee.com.br.issoebrasil.R;
import ieb.coffee.com.br.issoebrasil.helper.Preferencias;
import ieb.coffee.com.br.issoebrasil.model.Mensagem;

public class MensagemAdapter extends ArrayAdapter<Mensagem> {

        private Context context;
        private ArrayList<Mensagem> mensagens;


        public MensagemAdapter(Context c, ArrayList<Mensagem> objects) {
            super(c, 0, objects);
            this.context = c;
            this.mensagens = objects;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = null;
            //verificar se a lista está preenchida
            if(mensagens != null){

                //Recuperar dados do usuario remetente
                Preferencias preferencias = new Preferencias(context);
                String idUsuarioRemetente = preferencias.getIdentificador();

                // Inicializa objeto para montagem do layout
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

                //Recuperar mensagem
                Mensagem mensagem = mensagens.get(position);

                if(idUsuarioRemetente.equals(mensagem.getIdUsuario())){
                    //Montando view a partir do xml
                    view = inflater.inflate(R.layout.item_mensagem_direita, parent, false);
                }else{
                    view = inflater.inflate(R.layout.item_mensagem_esquerda, parent, false);
                }

                //Recuperar elemento para exibição
                TextView textoMensagem = (TextView) view.findViewById(R.id.tv_mensagem);
                textoMensagem.setText(mensagem.getMensagem());

            }

            return view;
        }
    }
