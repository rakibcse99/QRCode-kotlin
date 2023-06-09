  package com.rakib.qrcode.Fragments

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ListView
import com.rakib.qrcode.*

  class GenerateFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.theme?.applyStyle(R.style.Theme_Scan, false)
        //activity?.title = "Generate"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        activity?.window?.statusBarColor =  Color.parseColor("#ffffff");

        val view: View = inflater.inflate(R.layout.fragment_generate, container, false)

        val addButton: ImageButton = view.findViewById(R.id.add_button)
        val listView: ListView = view.findViewById(R.id.listView)

        addButton.setOnClickListener() {
            val fragment = addQRCodeFragment()
            activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.frame, fragment)
                    ?.commit();
        }

        val database = QrCodeDatabase(requireContext(), "myCode")

        val data = database.readMyCode(requireContext())

        var adapter: ListAdapter = ListAdapter(
                requireActivity(),
                R.layout.row,
                data
        )

        if (data.isNotEmpty()) {
            listView.adapter = adapter
        }

        listView.setOnItemClickListener {parent, view, position, id ->
            var id = data[position]
            //val deleteDialog = DeleteDialog(requireContext(), adapter, database.TABLE_MY_CODE)
            //adapter = deleteDialog.show(data[position], database, id.id)
            //adapter.notifyDataSetChanged()
            startFragment(id.title, id.desc, "barcode")
        }

        listView.emptyView = view.findViewById(R.id.emptyElement);

        return view
    }

      fun startFragment(type: String, data: String, codeFormat: String) { //add code type (barcode, qrcode) použiva sa viacktrat
          val bundle = Bundle()
          bundle.putString("QrType", type)
          bundle.putString("QrData", data)
          bundle.putString("CodeType", codeFormat)

          val fragment = ShowFromDatabaseFragment()
          fragment.arguments = bundle
          activity?.supportFragmentManager?.beginTransaction()
                  ?.replace(R.id.frame, fragment)
                  ?.commit();
      }
}

