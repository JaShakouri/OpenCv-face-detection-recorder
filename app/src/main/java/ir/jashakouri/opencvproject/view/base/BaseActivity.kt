package ir.jashakouri.opencvproject.view.base

import android.os.Bundle
import android.os.PersistableBundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    @LayoutRes
    abstract fun layout(): Int

    abstract fun create(savedInstanceState: Bundle?)

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        create(savedInstanceState)
        setContentView(layout())
    }

}