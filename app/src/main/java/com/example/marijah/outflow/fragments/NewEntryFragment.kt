package com.example.marijah.outflow.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.marijah.outflow.R
import com.example.marijah.outflow.adapters.CategoryAdapter
import com.example.marijah.outflow.helpers.showToast
import com.example.marijah.outflow.models.AppManager
import com.firebase.ui.auth.AuthMethodPickerLayout
import com.firebase.ui.auth.AuthUI
import kotlinx.android.synthetic.main.fragment_new_entry.*
import java.text.SimpleDateFormat
import java.util.*


class NewEntryFragment : Fragment(R.layout.fragment_new_entry_single) {

    // view model vazi za sve
    private lateinit var viewModel: NewEntryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[NewEntryViewModel::class.java]


        viewModel.userShouldSignUp.observe(this, androidx.lifecycle.Observer { shouldSignUp ->

            if (shouldSignUp) {
                val customLayout = AuthMethodPickerLayout.Builder(R.layout.activity_firebase_sign_in)
                        .setGoogleButtonId(R.id.txtViewGmailSignIn)
                        .setEmailButtonId(R.id.txtViewEmailSignIn)
                        .build()

                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setIsSmartLockEnabled(false)
                                .setTheme(R.style.FirebaseAuthTheme)
                                .setAuthMethodPickerLayout(customLayout)
                                .setAvailableProviders(Arrays.asList(
                                        AuthUI.IdpConfig.GoogleBuilder().build(),
                                        AuthUI.IdpConfig.EmailBuilder().build()))
                                .build(),
                        viewModel.RC_SIGN_IN)
            }
        })

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        context?.let {
            setLayoutAndListeners()
            /**Kreiramo adapter prosledjujuci niz elemenata */
            val adapter = CategoryAdapter(it, viewModel.categoryList)
            /**Povezujemo adapter sa RecyclerViewom */
            rlListCategory.adapter = adapter
            /**Postavljamo layout manager za nas RecyclerView */
            val layoutManager = LinearLayoutManager(it, LinearLayoutManager.HORIZONTAL, false)
            rlListCategory.layoutManager = layoutManager
        }
    }


    private fun setLayoutAndListeners() {

        imgViewSettings.setOnClickListener {
            findNavController().navigate(R.id.action_newEntrySingleFragment_to_settingsActivity)
        }

        imgViewList.setOnClickListener {
            findNavController().navigate(R.id.action_newEntrySingleFragment_to_listOfExpensesFragment)
        }


        /** Ako je korisnik kliknuo da doda novi racun*/
        txtViewAddNewExpenseButton.setOnClickListener {

            viewModel.testDataBinding.value = editTextComment.text.toString()

            /*// trazimo od njega da popuni sva polja
            if (editTextAmount.text.toString().isEmpty() || categoryPickedObject.categoryPicked.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill in all the required fields", Toast.LENGTH_SHORT).show()
            } else {

                // ispitujemo u kom je rezimu korisnik
                if (AppManager.getInstance(context).hasUserPickedSingleMode) {
                    // ako je u pojedinačnom režimu dodajemo trošak u lokalnu bazu
                    viewModel.insertExpenseInRoomDatabase(Expense(0, Integer.parseInt(editTextAmount.text.toString()),
                            categoryPickedObject.categoryPicked,
                            editTextStore.text.toString(),
                            txtViewDate.text.toString(),
                            editTextComment.text.toString()))
                } else {

                    val expenseItemID: String = viewModel.myReferenceToExpenses?.push()?.key ?: " "
                    viewModel.insertExpenseInFirebaseRealtimeDatabase(ExpenseItem(expenseItemID, Integer.parseInt(editTextAmount.text.toString()),
                            categoryPickedObject.categoryPicked,
                            editTextStore.text.toString(),
                            txtViewDate.text.toString(),
                            editTextComment.text.toString(),
                            AppManager.getInstance(context).currentlyLoggedInUserEmail))

                }

                context?.let {
                    showToast(it, "Item successfully added.")
                }
                cleanTheFields()
            }
*/

        }

        imgViewPie.setOnClickListener {
            findNavController().navigate(R.id.action_newEntrySingleFragment_to_statsSingleFragment)
        }

        //HelperManager.setTypefaceRegular(requireContext()!!.assets, txtViewName)


        txtViewDate.text = getTodaysDate()
        txtViewDate.setOnClickListener {
            addDateToLocalDatabase()
        }

    }

    /**
     * Funkcija za dodavanje item-a u lokalnu bazu podataka.
     */
    private fun addDateToLocalDatabase() {
        context?.let {
            val newDate = Calendar.getInstance()

            val dpd = DatePickerDialog(it, AlertDialog.THEME_HOLO_LIGHT, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                run {

                    val date: String = if (dayOfMonth < 10) {
                        "0$dayOfMonth"
                    } else {
                        dayOfMonth.toString()
                    }

                    val monthOfTheYear: String = if (month < 9) {
                        "0${month + 1}"
                    } else {
                        "${month + 1}"
                    }
                    val newDate = "$date.$monthOfTheYear.$year."
                    txtViewDate.text = newDate

                }
            }, newDate.get(Calendar.YEAR), newDate.get(Calendar.MONTH), newDate.get(Calendar.DAY_OF_MONTH))


            dpd.show()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == viewModel.RC_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                context?.let {
                    showToast(it, "You are now signed in. Welcome to Outflow!")
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                findNavController().popBackStack()
                context?.let {
                    showToast(it, "Sign In Cancelled!")
                }
            }

        }
    }

    override fun onResume() {
        super.onResume()
        if (!AppManager.getInstance(context).hasUserPickedSingleMode)
            viewModel.mAuthStateListener?.let {
                viewModel.mFirebaseAuth?.addAuthStateListener(it)
            }
    }

    override fun onPause() {
        super.onPause()
        if (!AppManager.getInstance(context).hasUserPickedSingleMode)
            viewModel.mAuthStateListener?.let {
                viewModel.mFirebaseAuth?.removeAuthStateListener(it)
            }
    }


    @SuppressLint("SimpleDateFormat")
    private fun getTodaysDate(): String {

        val c = Calendar.getInstance().time
        val df = SimpleDateFormat("dd.MM.yyyy.")
        return df.format(c)
    }


    private fun cleanTheFields() {
        editTextAmount.text.clear()
        editTextStore.text.clear()
        txtViewDate.text = getTodaysDate()
        editTextComment.text.clear()
    }


/*    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_entry, container, false) as ActivityNewEntryBinding
        val view: View = binding.getRoot()
        //here data must be an instance of the class MarsDataProvider
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return view
    }*/


}
