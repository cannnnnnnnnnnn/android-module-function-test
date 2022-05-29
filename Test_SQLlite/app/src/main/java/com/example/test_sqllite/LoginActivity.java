package com.example.test_sqllite;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.test_sqllite.tools.DBHelper;
import com.example.test_sqllite.tools.UserModel;

public class LoginActivity extends AppCompatActivity {

    private EditText user;
    private EditText password;

    private Button login;
    private Button regs;
    private Button update;
    private Button del;


    // 创建数据库对象
    DBHelper mySqliteHelper = new DBHelper(LoginActivity.this, "user.db", null, 1);

    UserModel data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 绑定控件对象
        user = findViewById(R.id.user);
        password = findViewById(R.id.password);

        login = findViewById(R.id.login);
        regs = findViewById(R.id.registered);
        update = findViewById(R.id.update);
        del = findViewById(R.id.delete);


        data = new UserModel();


        // 登录读取查询数据库
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = user.getText().toString().trim();
                String pass = password.getText().toString().trim();
                if (username != "" && pass != "") {
                    data.setUsername(username);
                    data.setPassword(pass);
                    if (queryPersonData(data)) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginActivity.this, "用户名或密码错误！", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        // 读取插入数据库
        regs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = user.getText().toString().trim();
                String pass = password.getText().toString().trim();
                if (username != "" && pass != "") {
                    data.setUsername(username);
                    data.setPassword(pass);
                    if (addPersonData(data)) {
                        Toast.makeText(LoginActivity.this, "添加成功！", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "添加失败！", Toast.LENGTH_SHORT).show();
                    }
                    // 清空输入框
                    user.setText("");
                    password.setText("");
                } else {
                    Toast.makeText(LoginActivity.this, "用户名和密码不能为空！", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 更新数据
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = user.getText().toString().trim();
                String pass = password.getText().toString().trim();
                if (upPersonData(username, pass)) {
                    Toast.makeText(LoginActivity.this, "更新成功！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "该账户不存在！", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 删除
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = user.getText().toString().trim();
                if (delPersonData(username)) {
                    Toast.makeText(LoginActivity.this, "删除成功！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "该账户不存在！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * @param model 数据模型
     * @return 返回添加数据有木有成功
     */
    public boolean addPersonData(UserModel model) {
        //把数据添加到ContentValues
        ContentValues values = new ContentValues();
        values.put("user", model.getUsername());
        values.put("password", model.getPassword());

        //添加数据到数据库
        long index = mySqliteHelper.getWritableDatabase().insert("users", null, values);

        //大于0表示添加成功
        return index > 0;
    }

    /**
     * 一些查询用法
     */
    public Boolean queryPersonData(UserModel model) {
        Boolean flag = false;
        // 接收查询的全部数据
        Cursor cursor = mySqliteHelper.getWritableDatabase().query("users", null, "user = ?", new String[]{data.getUsername()}, null, null, null);
        if (cursor.getCount() > 0) {
            //移动到首位
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                @SuppressLint("Range") String pass2 = cursor.getString(cursor.getColumnIndex("password"));
                // 验证密码
                if (pass2.equals(data.getPassword())) {
                    flag = true;
                }
                // 移动到下一个对象
                cursor.moveToNext();
            }
        }

        return flag;
    }

    /**
     * 删除表单信息
     */
    public Boolean delPersonData(String count) {
        Boolean flag = false;
        // 直接删除
        int result = mySqliteHelper.getWritableDatabase().delete("users", "user = ?", new String[]{count});
        if (result != 0) {
            flag = true;
        }
        return flag;
    }


    /**
     * 更新表单信息
     * 通过账户信息更新登录密码
     */
    public Boolean upPersonData(String count, String newPass) {
        Boolean flag = false;
        // 先进行查询
        // 查询是否存在该数据
        Cursor cursor = mySqliteHelper.getWritableDatabase().query("users", null, "user = ?", new String[]{count}, null, null, null);
        if (cursor.getCount() > 0) {
            // 存在该账户
            //把数据添加到ContentValues
            ContentValues values = new ContentValues();
            values.put("password", newPass);
            // 执行更新操作
            int result = mySqliteHelper.getWritableDatabase().update("users", values, "user = ?", new String[]{count});
            if (result != 0) {
                flag = true;
            }

        }
        return flag;
    }
}