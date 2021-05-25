package com.example.sqldemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {


    Button btn_viewall, btn_add;
    EditText  CustomerName , CustomerAge;
    Switch sw_active;
    ListView lv_cutomerview;
    ArrayAdapter customerArrayAdapter;
    DataBaseHelper dataBaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_add =findViewById(R.id.btn_add);
        btn_viewall=findViewById(R.id.btn_viewall);
        CustomerName=findViewById(R.id.CustomerName);
        CustomerAge=findViewById(R.id.Customer_Age);


        sw_active=findViewById(R.id.sw_active);
        lv_cutomerview =findViewById(R.id.lv_cutomerview);

        ShowCustomersOnListView(dataBaseHelper);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomerModel customerModel;
                try{

                     customerModel = new CustomerModel(-1, CustomerName.getText().toString(), Integer.parseInt(CustomerAge.getText().toString()),sw_active.isChecked());

//                    Toast.makeText(MainActivity.this,customerModel.toString(),Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){
                    Toast.makeText(MainActivity.this,"Error creating customer",Toast.LENGTH_SHORT).show();
                    customerModel = new CustomerModel(-1,null,0,false);

                }

                dataBaseHelper = new DataBaseHelper(MainActivity.this);
                 Boolean success=  dataBaseHelper.addOne(customerModel);

                Toast.makeText(MainActivity.this,"Success = "+ success,Toast.LENGTH_SHORT).show();
                ShowCustomersOnListView(dataBaseHelper);


            }
        });

        btn_viewall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                Toast.makeText(MainActivity.this,everyone.toString(),Toast.LENGTH_SHORT).show();

                ShowCustomersOnListView(dataBaseHelper);

                lv_cutomerview.setAdapter(customerArrayAdapter);
            }
        });

        lv_cutomerview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CustomerModel clickedCustomer = (CustomerModel)parent.getItemAtPosition(position);
                dataBaseHelper.deleteOne(clickedCustomer);
                ShowCustomersOnListView(dataBaseHelper);
                Toast.makeText(MainActivity.this,"Deleted "+clickedCustomer.toString(),Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void ShowCustomersOnListView(DataBaseHelper dataBaseHelper) {
        this.dataBaseHelper = new DataBaseHelper(MainActivity.this);
        List<CustomerModel> everyone = this.dataBaseHelper.getALlCustomers();
        customerArrayAdapter = new ArrayAdapter<CustomerModel>(MainActivity.this, android.R.layout.simple_list_item_1, everyone);
    }
}