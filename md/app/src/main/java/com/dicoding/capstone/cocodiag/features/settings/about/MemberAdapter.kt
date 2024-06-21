package com.dicoding.capstone.cocodiag.features.settings.about

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.capstone.cocodiag.databinding.ItemMemberBinding

class MemberAdapter(private val memberList: List<Member>) :
    RecyclerView.Adapter<MemberAdapter.MemberViewHolder>() {

    class MemberViewHolder(private val binding: ItemMemberBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(member: Member) {
            binding.nameMember.text = member.name
            binding.university.text = member.university
            binding.path.text = member.path
            binding.imgMember.setImageResource(member.image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        val binding= ItemMemberBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MemberViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        holder.bind(memberList[position])

    }

    override fun getItemCount(): Int {
        return memberList.size
    }

}