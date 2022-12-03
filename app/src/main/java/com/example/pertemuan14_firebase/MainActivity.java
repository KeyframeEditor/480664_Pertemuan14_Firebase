package com.example.pertemuan14_firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.support.v4.os.IResultReceiver;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private EditText name, address, updateName, updateAddress;
    private Button insert, read, update, readDosen;
    private RadioGroup radioGroup;
    private RadioButton radioButton;

    DatabaseReference databaseReferenceMahasiswa = FirebaseDatabase.getInstance().getReference(Mahasiswa.class.getSimpleName());
    DatabaseReference databaseReferenceDosen = FirebaseDatabase.getInstance().getReference(Dosen.class.getSimpleName());

    private String dbKey;
    private String currentShowData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.name_edittext);
        address = findViewById(R.id.address_edittext);
        updateName = findViewById(R.id.updatename_edittext);
        updateAddress = findViewById(R.id.updateaddress_edittext);

        insert = findViewById(R.id.insert_btn);
        read = findViewById(R.id.read_btn);
        readDosen = findViewById(R.id.readDosen_btn);
        update = findViewById(R.id.update_btn);

        radioGroup = findViewById(R.id.radio_group);

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertData();
            }
        });

        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readData("Mahasiswa");
            }
        });

        readDosen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readData("Dosen");
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData();
            }
        });
    }

    private void updateData() {


        if(currentShowData.equals("Mahasiswa")){
            Mahasiswa updateData = new Mahasiswa();
            updateData.setNamaMhs(updateName.getText().toString());
            updateData.setAlamat(updateAddress.getText().toString());

            databaseReferenceMahasiswa.child(dbKey).setValue(updateData);
            Toast.makeText(this, "Berhasil update mahasiswa", Toast.LENGTH_SHORT).show();
        }else if(currentShowData.equals("Dosen")){
            Dosen dosen = new Dosen();
            dosen.setNamaDosen(updateName.getText().toString());
            dosen.setAlamat(updateAddress.getText().toString());

            databaseReferenceDosen.child(dbKey).setValue(dosen);
            Toast.makeText(this, "Berhasil update Dosen", Toast.LENGTH_SHORT).show();
        }

        updateName.setText("");
        updateAddress.setText("");
    }

    private void readData(String text) {
        currentShowData = text;
        if (text.equals("Mahasiswa")){
            Mahasiswa mahasiswa = new Mahasiswa();
            databaseReferenceMahasiswa.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChildren()){
                        for (DataSnapshot currentData : snapshot.getChildren()){
                            mahasiswa.setNamaMhs(currentData.child("namaMhs").getValue().toString());
                            mahasiswa.setAlamat(currentData.child("alamat").getValue().toString());
                            dbKey = currentData.getKey();
                        }
                    }
                    updateName.setText(mahasiswa.getNamaMhs().toString());
                    updateAddress.setText(mahasiswa.getAlamat().toString());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MainActivity.this, "Error! "+error, Toast.LENGTH_SHORT).show();
                }
            });
        }else if (text.equals("Dosen")){
            Dosen dosen = new Dosen();
            databaseReferenceDosen.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChildren()){
                        for (DataSnapshot currentData : snapshot.getChildren()){
                            dosen.setNamaDosen(currentData.child("namaDosen").getValue().toString());
                            dosen.setAlamat(currentData.child("alamat").getValue().toString());
                            dbKey = currentData.getKey();
                        }
                    }
                    updateName.setText(dosen.getNamaDosen().toString());
                    updateAddress.setText(dosen.getAlamat().toString());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MainActivity.this, "Error! "+error, Toast.LENGTH_SHORT).show();
                }
            });
        }


    }

    private void insertData() {
        String editTextNama =  name.getText().toString();
        String editTextAddress =  address.getText().toString();

        if (checkButton("Mahasiswa")){
            Mahasiswa mahasiswa = new Mahasiswa();

            mahasiswa.setNamaMhs(editTextNama);
            mahasiswa.setAlamat(editTextAddress);
            Toast.makeText(this, "Added Mahasiswa", Toast.LENGTH_SHORT).show();

            databaseReferenceMahasiswa.push().setValue(mahasiswa);
        }else if (checkButton("Dosen")){
            Dosen dosen = new Dosen();

            dosen.setNamaDosen(editTextNama);
            dosen.setAlamat(editTextAddress);
            Toast.makeText(this, "Added Dosen", Toast.LENGTH_SHORT).show();

            databaseReferenceDosen.push().setValue(dosen);
        }

        name.setText("");
        address.setText("");
    }

    private boolean checkButton(String text){
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
        if (radioButton.getText().equals(text)){
            return true;
        }
        else{
            return false;
        }
    }
}