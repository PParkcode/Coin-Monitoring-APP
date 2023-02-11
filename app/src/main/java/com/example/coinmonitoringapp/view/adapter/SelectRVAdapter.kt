package com.example.coinmonitoringapp.view.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.coinmonitoringapp.R
import com.example.coinmonitoringapp.dataModel.CurrentPriceResult

class SelectRVAdapter(val context: Context, val coinPricelist: List<CurrentPriceResult>) :
    RecyclerView.Adapter<SelectRVAdapter.ViewHolder>() {

    //ViewHolder의 정확한 역할은 무엇인가?
    /**
     * RecyclerView는 항목 View가 ViewHolder 인스턴스에 포함되어 있다고 간주한다
     * ViewHolder는 항목 View의 참조를 갖는다.
     *
     * RecyclerView는 자체적으로 View를 생성하지 않으며, 항상 항목 View를 참조하는 ViewHolder를 생성한다.
     *
     * 어댑터는 ViewHolder 인스턴스들을 생성하며, 모델 계층의 데이터를 ViewHolder들과 바인딩한다,
     *
     * RecyclerView는 새로운 ViewHolder ㅇ니스턴스의 생성을 어댑터에게 요청한다.
     * 또한 지정된 위치의 데이터 항목에 ViewHolder 를 바인딩하도록 어댑터에게 요청한다
     */

    val selectedCoinList =ArrayList<String>()
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val coinName :TextView =view.findViewById(R.id.coinName)
        val coinPriceUpDown :TextView =view.findViewById(R.id.coinPriceUpDown)
        val likeImage : ImageView =view.findViewById(R.id.likeBtn)

    }

    //onCreateViewHolder의 역할은 무엇?

    /**
     * Adapter.onCreateViewHolder()는 보여줄 뷰를 인플레이트한 후 이 뷰를 처리하는 ViewHolder 인스턴스를 생성하고 반환한다.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.intro_coin_item, parent, false)
        return ViewHolder(view)
    }

    //onBindViewHolder의 역할은 무엇?

    /**
     * Adapter.onBindViewHolder()는 인자로 전달된 위치에 있는 모델 객체를 ViewHolder 인스턴스가 참조하는
     * 속성들을 설정 가능
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //ViewHolder는 정확히 무슨 역할?
        holder.coinName.text=coinPricelist[position].coinName

        val fluctate_24H = coinPricelist[position].coinInfo.fluctate_24H

        if(fluctate_24H.contains("-")){
            holder.coinPriceUpDown.text="하락입니다"
            holder.coinPriceUpDown.setTextColor(Color.parseColor("#114fed"))
        }
        else{
            holder.coinPriceUpDown.text="상승입니다"
            holder.coinPriceUpDown.setTextColor(Color.parseColor("#ed2e11"))
        }

        val likeImage =holder.likeImage
        val currentCoin= coinPricelist[position].coinName

        //view가 그려질 때
        if(selectedCoinList.contains(currentCoin)){
            likeImage.setImageResource(R.drawable.like_red)
        } else{
            likeImage.setImageResource(R.drawable.like_grey)
        }

        likeImage.setOnClickListener{

            if(selectedCoinList.contains(currentCoin)){
                //포함하면
                selectedCoinList.remove(currentCoin)
                likeImage.setImageResource(R.drawable.like_grey)
            }
            else{
                //포함하지 않으면
                selectedCoinList.add(currentCoin)
                likeImage.setImageResource(R.drawable.like_red)
            }

        }
   }

    override fun getItemCount(): Int {
        return coinPricelist.size
    }
}