package e.sergeev.oleg.salesmap

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import e.sergeev.oleg.salesmap.R.id.cvLogOn

import kotlinx.android.synthetic.main.activity_login1.*

/**
 * Created by o.sergeev on 23.05.2018.
 */

private var mAuthTask: LoginActivity1.UserLoginTask1? = null

class LoginActivity1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login1)

        cvLogOn.setOnClickListener {
            attemptLogin()
        }
    }

    private fun attemptLogin() {
        /*if (mAuthTask != null) {
            return
        }*/

        // Reset errors.
        edLogin.error = null
        edPass.error = null

        // Store values at the time of the login attempt.
        val emailStr = edLogin.text.toString()
        val passwordStr = edPass.text.toString()

        var cancel = false
        var focusView: View? = null

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(passwordStr) && !isPasswordValid(passwordStr)) {
            edPass.error = getString(R.string.error_invalid_password)
            focusView = edPass
            cancel = true
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(emailStr)) {
            edLogin.error = getString(R.string.error_field_required)
            focusView = edLogin
            cancel = true
        } else if (!isEmailValid(emailStr)) {
            edLogin.error = getString(R.string.error_invalid_email)
            focusView = edLogin
            cancel = true
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView?.requestFocus()
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true)
            mAuthTask = UserLoginTask1(emailStr, passwordStr)
            mAuthTask!!.execute(null as Void?)
        }
    }

    private fun isEmailValid(email: String): Boolean {
        //TODO: Replace this with your own logic
        return email.contains("r")
    }

    private fun isPasswordValid(password: String): Boolean {
        //TODO: Replace this with your own logic
        //return password.length > 4
        return true
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private fun showProgress(show: Boolean) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

            login_layout.visibility = if (show) View.GONE else View.VISIBLE
            login_layout.animate()
                    .setDuration(shortAnimTime)
                    .alpha((if (show) 0 else 1).toFloat())
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            login_layout.visibility = if (show) View.GONE else View.VISIBLE
                        }
                    })

            progress_login.visibility = if (show) View.VISIBLE else View.GONE
            progress_login.animate()
                    .setDuration(shortAnimTime)
                    .alpha((if (show) 1 else 0).toFloat())
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            progress_login.visibility = if (show) View.VISIBLE else View.GONE
                        }
                    })
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progress_login.visibility = if (show) View.VISIBLE else View.GONE
            login_layout.visibility = if (show) View.GONE else View.VISIBLE
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    inner class UserLoginTask1 internal constructor(private val mEmail: String, private val mPassword: String) : AsyncTask<Void, Void, Boolean>() {

        override fun doInBackground(vararg params: Void): Boolean? {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(3000)
            } catch (e: InterruptedException) {
                return false
            }

            return LoginActivity1.DUMMY_CREDENTIALS
                    .map { it.split(":") }
                    .firstOrNull { it[0] == mEmail }
                    ?.let {
                        // Account exists, return true if the password matches.
                        it[1] == mPassword
                    }
                    ?: true
        }

        override fun onPostExecute(success: Boolean?) {
            mAuthTask = null
            showProgress(false)

            if (success!!) {
                val intent = Intent(this@LoginActivity1,MainActivity::class.java)
                /*var userName = edLogin.text.toString()*/
                //var password = password_field.text.toString()
                intent.putExtra("terId", edLogin.text.toString())
                startActivity(intent)
                finish()
            } else {
                edLogin.error = getString(R.string.error_incorrect_password)
                edPass.requestFocus()
            }
        }

        override fun onCancelled() {
            mAuthTask = null
            showProgress(false)
        }
    }

    companion object {

        /**
         * Id to identity READ_CONTACTS permission request.
         */
        private val REQUEST_READ_CONTACTS = 0

        /**
         * A dummy authentication store containing known user names and passwords.
         * TODO: remove after connecting to a real authentication system.
         */
        private val DUMMY_CREDENTIALS = arrayOf("")
    }


}