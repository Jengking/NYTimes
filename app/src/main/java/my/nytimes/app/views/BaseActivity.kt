package my.nytimes.app.views

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun promptError(closeActivity: Boolean) {
        this.runOnUiThread {
            if (!isFinishing && !isDestroyed) {
                val info = AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setTitle("Error")
                    .setMessage("Oppss...something when wrong. Please try again later.")
                    .setNeutralButton("OK") { dialog, position ->
                        dialog.dismiss()
                        if (closeActivity) {
                            finish()
                        } else {
                            finishAndRemoveTask()
                        }
                    }.create()
                info.show()
            }
        }
    }
}