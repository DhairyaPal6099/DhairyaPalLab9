package dhairya.pal.n01576099.dp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import dhairya.pal.n01576099.dp.databinding.FragmentPa2lBinding;

public class Pa2l extends Fragment {
    private ToggleButton fileType;
    LinearLayout linearLayout;
    private EditText fileName, fileContents;
    private FragmentPa2lBinding binding;
    int counter = 0;
    ArrayList<TextView> textViews = new ArrayList<>();
    ArrayList<String> fileNames;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPa2lBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        fileName = binding.activityInternalstorageFilename;
        fileContents = binding.activityInternalstorageFilecontents;
        linearLayout = binding.dhaLinearLayout;

        fileType = binding.activityInternalstorageFiletype;
        fileType.setChecked(true);

        binding.activityInternalstorageCreate.setOnClickListener(view -> createFile(getContext().getApplicationContext(), fileType.isChecked()));
        binding.activityInternalstorageDelete.setOnClickListener(view -> deleteFile(getContext().getApplicationContext(), fileType.isChecked()));
        binding.activityInternalstorageWrite.setOnClickListener(view -> writeFile(getContext().getApplicationContext(), fileType.isChecked()));
        binding.activityInternalstorageRead.setOnClickListener(view -> readFile(getContext().getApplicationContext(), fileType.isChecked()));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void fileNameMissingCheck() {
        if (fileName.getText().toString().isEmpty()) {
            Snackbar snackbar = Snackbar.make(getView(), "Dhairya Pal file name missing", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("DISMISS", view -> snackbar.dismiss());
            snackbar.show();
        }
    }

    private void fileContentsMissingCheck() {
        if (fileContents.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Dhairya Pal content missing", Toast.LENGTH_LONG).show();
        }
    }

    private void createFile(Context context, boolean isPersistent) {
        if (textViews != null) {
            if (textViews.size() >= 3) {
                linearLayout.removeAllViews();
                textViews.remove(0);
                for (TextView textView : textViews) {
                    linearLayout.addView(textView);
                }
            }
        }

        fileNameMissingCheck();
        File file;
        if (isPersistent) {
            file = new File(context.getFilesDir(), fileName.getText().toString());
        } else {
            file = new File(context.getCacheDir(), fileName.getText().toString());
        }

        if (!file.exists()) {
            try {
                file.createNewFile();
                Toast.makeText(context, String.format("File %s has been created", fileName.getText().toString()), Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(context, String.format("File %s creation failed", fileName.getText().toString()), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, String.format("File %s already exists", fileName.getText().toString()), Toast.LENGTH_SHORT).show();
        }


        //Show file name logic
        Random random = new Random();
        int randomColor = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        TextView textViewx = new TextView(context);
        textViewx.setText(fileName.getText());
        textViewx.setTypeface(null, Typeface.BOLD_ITALIC);
        textViewx.setTextSize(20);
        textViewx.setTextColor(randomColor);
        textViewx.setLayoutParams(layoutParams);
        linearLayout.addView(textViewx);
        textViews.add(textViewx);
    }

    private void writeFile(Context context, boolean isPersistent) {
        fileNameMissingCheck();
        fileContentsMissingCheck();
        try {
            FileOutputStream fileOutputStream;
            if (isPersistent) {
                fileOutputStream = context.openFileOutput(fileName.getText().toString(), Context.MODE_PRIVATE);
            } else {
                File file = new File(context.getCacheDir(), fileName.getText().toString());
                fileOutputStream = new FileOutputStream(file);
            }
            fileOutputStream.write(fileContents.getText().toString().getBytes(Charset.forName("UTF-8")));
            Toast.makeText(context, String.format("Write to %s successful", fileName.getText().toString()), Toast.LENGTH_SHORT).show();
            fileContents.setText("");
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, String.format("Write to file %s failed", fileName.getText().toString()), Toast.LENGTH_SHORT).show();
        }
    }

    private void readFile(Context context, boolean isPersistent) {
        fileNameMissingCheck();
        try {
            FileInputStream fileInputStream;
            if (isPersistent) {
                fileInputStream = context.openFileInput(fileName.getText().toString());
            } else {
                File file = new File(context.getCacheDir(), fileName.getText().toString());
                fileInputStream = new FileInputStream(file);
            }

            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, Charset.forName("UTF-8"));
            List<String> lines = new ArrayList<String>();
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                lines.add(line);
                line = reader.readLine();
            }
            fileContents.setText(TextUtils.join("\n", lines));
            Toast.makeText(context, String.format("Read from file %s successful", fileName.getText().toString()), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, String.format("Read from file %s failed", fileName.getText().toString()), Toast.LENGTH_SHORT).show();
            fileContents.setText("");

        }
    }

    private void deleteFile(Context context, boolean isPersistent) {
        fileNameMissingCheck();
        linearLayout.removeAllViews();
        for (TextView x : textViews) {
            if (x.getText().equals(fileName.getText().toString())) {
                textViews.remove(x);
            }
        }
        for (TextView textView : textViews) {
            linearLayout.addView(textView);
        }


        File file;
        if (isPersistent) {
            file = new File(context.getFilesDir(), fileName.getText().toString());
        } else {
            file = new File(context.getCacheDir(), fileName.getText().toString());
        }
        if (file.exists()) {
            file.delete();
            Toast.makeText(context, String.format("File %s has been deleted", fileName.getText().toString()), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, String.format("File %s doesn't exist", fileName.getText().toString()), Toast.LENGTH_SHORT).show();
        }
    }
}