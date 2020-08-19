package codepath.simpletodo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
//import android.os.FileUtils;
import org.apache.commons.io.FileUtils;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    public static final String KEY_ITEM_TEXT ="item_text";
    public static final String KEY_ITEM_POSITION = "item_position";
    public static final int EDIT_TEXT_CODE = 20;


    List<String> items;
    Button btnAdd;
    EditText edItem;
    RecyclerView rvItems;
    itemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        edItem = findViewById(R.id.edItem);
        rvItems = findViewById(R.id.rvItems);


        loadItems();


        itemsAdapter.OnLongClickListener onLongClickListener = new itemsAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClicked(int position) {
                    //delete item from model and notify adapter
                items.remove(position);
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "Item was removed", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };
        itemsAdapter.OnClickListener onClickListener = new itemsAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                Log.d("MainActivity","single click a position" + position);
                //create the new activity
                Intent i = new Intent(MainActivity.this, EditActivity.class);
                //pass the data being edited
                i.putExtra(KEY_ITEM_TEXT, items.get(position));
                i.putExtra(KEY_ITEM_POSITION, position);

                // display the activity
                startActivityForResult(i, EDIT_TEXT_CODE);
            }
        };



        itemsAdapter = new itemsAdapter(items, onLongClickListener, onClickListener);

        rvItems.setAdapter(itemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String todoItem = edItem.getText().toString();
                //add item to model
                items.add(todoItem);

                // notify adapter that item has been inserted
                itemsAdapter.notifyItemInserted(items.size() -1);
                edItem.setText("");
                Toast.makeText(getApplicationContext(), "Item was add", Toast.LENGTH_SHORT).show();
                saveItems();

            }
        });

    }

    //handle the result of edit activity
    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE){
            //retrieve the updated text value
            String itemText = data.getStringExtra(KEY_ITEM_TEXT);

            //extract original position of edited item from the position key
            int position = data.getExtras().getInt(KEY_ITEM_POSITION);
            //update the model with new item text
            items.set(position, itemText);
            //notify the adaptor
            itemsAdapter.notifyItemChanged(position);

            // persist changes
            saveItems();
            Toast.makeText(getApplicationContext(), "Item updated successfully!", Toast.LENGTH_SHORT).show();

        } else  {
            Log.w("MainActivity", "unkown call to onActivityResult");
        }
    }

    private File getDataFile(){
        return new File(getFilesDir(), "data.txt");

    }
    //this function will load items by reading every line of the data file
    private void loadItems(){
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "error reading items", e);
            items = new ArrayList<>();

        }

    }
    //this function saves items by writing them into the data file
    private void saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing items", e);
        }

    }
}