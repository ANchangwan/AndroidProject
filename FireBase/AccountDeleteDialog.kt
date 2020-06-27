package weather.Anchangwan1501023.FireBase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.account_delete_dialog.*
import java.nio.file.Files.delete

class AccountDeleteDialog : DialogFragment() {

    interface  AccountDeleteDialoginterface{
        fun delete()
        fun cancelDelete()
    }
    private var accountDeleteDialoginterface:AccountDeleteDialoginterface? =null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.account_delete_dialog, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupListener()
    }

    fun addAccountDeleteDialoginterface(Listener: AccountDeleteDialoginterface){
        accountDeleteDialoginterface = Listener
    }

    private fun setupListener() {
        delete_no.setOnClickListener {
            accountDeleteDialoginterface?.cancelDelete()
            dismiss()
        }

        delete_yes.setOnClickListener {
            accountDeleteDialoginterface?.delete()
            dismiss()
        }

    }



}