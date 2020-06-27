package weather.Anchangwan1501023.FireBase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.firebase.ui.auth.AuthUI
import kotlinx.android.synthetic.main.activity_account_setting.*
import java.nio.file.Files.delete

class AccountSettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_setting)

        setupListener()

    }

    private fun setupListener(){
        Account_setting_back.setOnClickListener { onBackPressed() }
        Account_setting_Logout.setOnClickListener { signoutAccount() }
        Account_setting_delete.setOnClickListener { showDeletDialog() }
    }

    private fun signoutAccount() {
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener {
                Toast.makeText(this,"로그아웃 했습니다.",Toast.LENGTH_SHORT).show()
                moveToMainActivity()

            }
    }

    private fun deleteAccount() {

       AuthUI.getInstance()
            .delete(this)
            .addOnCompleteListener {
               Toast.makeText(this,"계정탈퇴 했습니다.",Toast.LENGTH_SHORT).show()
                moveToMainActivity()
            }
    }
private fun showDeletDialog() {
    AccountDeleteDialog().apply {
        addAccountDeleteDialoginterface(object : AccountDeleteDialog.AccountDeleteDialoginterface{
            override fun delete() {
                deleteAccount()

            }

            override fun cancelDelete() {

            }
        })
    }.show(supportFragmentManager,"")
}

    private fun moveToMainActivity() {
        startActivity(Intent(this, MainActivity :: class.java))
    }

}


