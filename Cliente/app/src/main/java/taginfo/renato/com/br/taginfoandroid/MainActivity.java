package taginfo.renato.com.br.taginfoandroid;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Locale;

import taginfo.renato.com.br.taginfoandroid.http.cliente.HttpCliente;
import taginfo.renato.com.br.taginfoandroid.http.cliente.HttpExcecao;
import taginfo.renato.com.br.taginfoandroid.http.cliente.HttpJson;
import taginfo.renato.com.br.taginfoservice.mensagem.MensagemInformacao;
import taginfo.renato.com.br.taginfoservice.modelo.InformacaoProduto;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private NfcAdapter nfcAdapter;
    private ProgressDialog progressDialog;

    private final String[][] techList = new String[][]{
            new String[]{
                    NfcA.class.getName(),
                    NfcB.class.getName(),
                    NfcF.class.getName(),
                    NfcV.class.getName(),
                    IsoDep.class.getName(),
                    MifareClassic.class.getName(),
                    MifareUltralight.class.getName(), Ndef.class.getName()
            }
    };

    private IntentFilter intentFilterNfc = new IntentFilter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        progressDialog = new ProgressDialog(this, R.style.DialogTransparent);
        progressDialog.setMessage(getString(R.string.mensagem_consultando_tag));

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (nfcAdapter == null) {
            showMessage(R.string.nfc_nao_suportado, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
        }

        intentFilterNfc.addAction(NfcAdapter.ACTION_TAG_DISCOVERED);
        intentFilterNfc.addAction(NfcAdapter.ACTION_TECH_DISCOVERED);
        intentFilterNfc.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent != null && intent.getAction() != null && intent.getAction().contains("android.nfc.action")) {

            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

            byte[] idTag = tag.getId();

            AsyncTaskInformacaoTag asyncTaskInformacaoTag = new AsyncTaskInformacaoTag();

            asyncTaskInformacaoTag.execute(bytesToHexString(idTag));
        }
    }

    private String bytesToHexString(byte[] idTag) {

        StringBuffer bf = new StringBuffer();

        for (int i = 0; i < idTag.length; i++) {
            bf.append(String.format("%02X:", idTag[i]));
        }

        return bf.toString();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private PendingIntent getPendingIntent() {
        return PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (nfcAdapter.isEnabled()) {
            habilitarNfc();
        } else {
            showNFCSettingsDialog();
        }
    }

    private void habilitarNfc() {
        nfcAdapter.enableForegroundDispatch(this, getPendingIntent(), new IntentFilter[]{intentFilterNfc}, null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        desabilitarNfc();
    }

    private void desabilitarNfc() {
        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
        }
    }

    private void showNFCSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.nfc_disabilitado);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Settings.ACTION_NFC_SETTINGS);
                startActivity(intent);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.create().show();
        return;
    }

    private void showMessage(int idMenssage) {

        showMessage(idMenssage, null);
    }

    private void showMessage(int idMenssage, DialogInterface.OnClickListener clickListener) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(idMenssage);
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        if (clickListener != null) {
            builder.setPositiveButton(android.R.string.ok, clickListener);
        }
        builder.create().show();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class AsyncTaskInformacaoTag extends AsyncTask<String, Void, MensagemInformacao> {

        private Exception exception;

        public AsyncTaskInformacaoTag() {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected MensagemInformacao doInBackground(String... tagId) {

            HttpJson httpJson = new HttpJson();

            try {
                return httpJson.enviarGet(MensagemInformacao.class, HttpCliente.getUrlInformacao(tagId[0]));
            } catch (Exception e) {
                Log.e(MainActivity.class.getSimpleName(), "AsyncTaskInformacaoTag.doInBackground", e);
                this.exception = e;
            }
            return null;
        }

        @Override
        protected void onPostExecute(MensagemInformacao mensagemInformacao) {
            super.onPostExecute(mensagemInformacao);
            try {
                if (exception == null && mensagemInformacao != null && mensagemInformacao.getInformacaoProduto() != null) {

                    Bundle bundle = new Bundle();

                    bundle.putSerializable(InformacaoProduto.class.getName(), mensagemInformacao.getInformacaoProduto());

                    Intent intent = new Intent(MainActivity.this, InfoProdutoActivity.class);
                    intent.putExtras(bundle);

                    startActivity(intent);
                } else {
                    showMessage(R.string.alerta_falha_obter_informacao);
                }
            } finally {
                progressDialog.hide();
            }
        }
    }
}
