package com.example.resumaker

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.BaseAdapter
import android.widget.ImageView

//import com.bumptech.glide.Glide

abstract class GridViewAdapter(
    private val mContext: Context,
    private val templates: ArrayList<Category.Template>
) : BaseAdapter() {

    /*private val storageReference: StorageReference? = null*/
    private lateinit var inflater: LayoutInflater

    private class ViewHolder(view: View) {
        val templateIV: ImageView = view.findViewById(R.id.temp_classic_img)
    }

    override fun getCount(): Int {
        return templates.size
    }

    override fun getItem(position: Int): Any {
        return templates[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}

    //override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        //val holder: ViewHolder

        //val view = convertView ?: run {
            //inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            //val newView = inflater.inflate(R.layout.resume_temp_list, parent, false)
            //holder = ViewHolder(newView)
            //newView.tag = holder
            //newView
        //}.also { holder = it.tag as ViewHolder }

        //val imagePath = templates[position].imgPath
        //val storageRef = .getInstance().getReference(imagePath)

        //Glide.with(mContext)
            //.load(storageRef)
            //.into(holder.templateIV)

        //return view
    //}
//}