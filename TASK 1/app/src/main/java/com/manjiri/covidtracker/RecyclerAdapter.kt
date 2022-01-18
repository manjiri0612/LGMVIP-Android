package com.manjiri.covidtracker

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter(context: Context, val stateList: ArrayList<StateCases>) : RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder>() {
    class RecyclerViewHolder(view: View): RecyclerView.ViewHolder(view){
        val txtStateName: TextView =view.findViewById(R.id.txtStateName)
        val txtTotalCasesNo: TextView =view.findViewById(R.id.txtTotalCasesNo)
        val txtRecoveredCasesNo: TextView =view.findViewById(R.id.txtRecoveredCasesNo)
        val txtDeathCasesNo: TextView =view.findViewById(R.id.txtDeathCasesNo)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerAdapter.RecyclerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.state_one_row, parent, false)

        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.RecyclerViewHolder, position: Int) {
        val covidInfo = stateList[position]
        holder.txtStateName.text = covidInfo.stateName
        holder.txtTotalCasesNo.text = covidInfo.totalcases.toString()
        holder.txtRecoveredCasesNo.text = covidInfo.recovered.toString()
        holder.txtDeathCasesNo.text = covidInfo.deaths.toString()

    }

    override fun getItemCount(): Int {
        return stateList.size
    }


}
