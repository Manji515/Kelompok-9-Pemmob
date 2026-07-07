package com.example.rekraprima

import android.content.Context
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class PengajuanAdapter(
    private val list: List<PengajuanModel>,
    private val onItemClick: ((PengajuanModel, String) -> Unit)? = null
) : RecyclerView.Adapter<PengajuanAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvKategori: TextView = view.findViewById(R.id.tvItemKategori)
        val tvTujuan: TextView = view.findViewById(R.id.tvItemTujuan)
        val tvTanggal: TextView = view.findViewById(R.id.tvItemTanggal)
        val tvAgenda: TextView = view.findViewById(R.id.tvItemAgenda)
        val tvStatus: TextView = view.findViewById(R.id.tvItemStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pengajuan, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        val context = holder.itemView.context

        val bagianKategori = item.agenda.split("|")
        var kategoriSekarang = "AJUAN"
        var dataMentahTeks = item.agenda

        if (bagianKategori.size == 2) {
            kategoriSekarang = bagianKategori[0]
            holder.tvKategori.text = kategoriSekarang.uppercase()
            dataMentahTeks = bagianKategori[1]
        } else {
            holder.tvKategori.text = kategoriSekarang
        }

        val bagianSurat = dataMentahTeks.split("@@@")
        val teksKiri = bagianSurat[0]
        val nomorSuratTerbit = if (bagianSurat.size == 2) bagianSurat[1] else ""

        val bagianAlasan = teksKiri.split("###")
        val deskripsiKomponenAsli = bagianAlasan[0]
        val alasanTolakTerbaca = if (bagianAlasan.size == 2) bagianAlasan[1] else ""

        val detailTengahBuilder = SpannableStringBuilder()
        val detailBawahBuilder = SpannableStringBuilder()

        when (kategoriSekarang.trim().lowercase()) {
            "perjalanan dinas" -> {
                holder.tvTujuan.text = "Tujuan Dinas: ${item.tujuan}"

                detailTengahBuilder.append(createSpannedText(context, R.drawable.ic_calendar, "Berangkat: ${item.tglBerangkat}\n"))
                detailTengahBuilder.append(createSpannedText(context, R.drawable.ic_calendar, "Kembali: ${item.tglKembali}"))

                detailBawahBuilder.append(createSpannedText(context, R.drawable.ic_assignment, "Agenda: $deskripsiKomponenAsli"))
            }
            "logistik barang" -> {
                holder.tvTujuan.text = "Nama Barang: ${item.tujuan}"

                detailTengahBuilder.append(createSpannedText(context, R.drawable.ic_numbers, "Jumlah: ${item.tglBerangkat} Pcs\n"))
                detailTengahBuilder.append(createSpannedText(context, R.drawable.ic_lightbulb, "Alasan Kebutuhan: ${item.tglKembali}"))

                detailBawahBuilder.append(createSpannedText(context, R.drawable.ic_assignment, "Keterangan: $deskripsiKomponenAsli"))
            }
            "penomoran dokumen" -> {
                holder.tvTujuan.text = "Judul Dokumen: ${item.tujuan}"

                detailTengahBuilder.append(createSpannedText(context, R.drawable.ic_calendar, "Tanggal: ${item.tglBerangkat}\n"))
                detailTengahBuilder.append(createSpannedText(context, R.drawable.ic_folder, "Jenis Dokumen: ${item.tglKembali}"))

                detailBawahBuilder.append(createSpannedText(context, R.drawable.ic_assignment, "Perihal: $deskripsiKomponenAsli"))
            }
            "pengajuan kendaraan dinas" -> {
                holder.tvTujuan.text = "Tujuan Berangkat: ${item.tujuan}"

                detailTengahBuilder.append(createSpannedText(context, R.drawable.ic_calendar, "Berangkat: ${item.tglBerangkat}\n"))
                detailTengahBuilder.append(createSpannedText(context, R.drawable.ic_car, "Jenis Kendaraan: ${item.tglKembali}"))

                detailBawahBuilder.append(createSpannedText(context, R.drawable.ic_assignment, "Agenda: $deskripsiKomponenAsli"))
            }
        }

        // Menampilkan alasan penolakan atau surat diterbitkan jika ada
        if (alasanTolakTerbaca.isNotEmpty()) {
            if (kategoriSekarang.trim().lowercase() == "penomoran dokumen" && item.status.lowercase().trim() == "surat telah diterbitkan") {
                detailBawahBuilder.append("\n").append(createSpannedText(context, R.drawable.ic_mail, "Nomor Resmi: $alasanTolakTerbaca"))
            } else {
                detailBawahBuilder.append("\n").append(createSpannedText(context, R.drawable.ic_cancel, "Alasan Tolak: $alasanTolakTerbaca"))
            }
        }
        if (nomorSuratTerbit.isNotEmpty()) {
            detailBawahBuilder.append("\n").append(createSpannedText(context, R.drawable.ic_mail, "Dokumen Surat: $nomorSuratTerbit"))
        }

        holder.tvTanggal.text = detailTengahBuilder
        holder.tvAgenda.text = detailBawahBuilder
        holder.tvStatus.text = item.status

        // Warna Status
        when (item.status.lowercase().trim()) {
            "diterima", "disetujui" -> {
                holder.tvStatus.setTextColor(Color.parseColor("#1B5E20"))
                holder.tvStatus.setBackgroundColor(Color.parseColor("#C8E6C9"))
            }
            "ditolak" -> {
                holder.tvStatus.setTextColor(Color.parseColor("#B71C1C"))
                holder.tvStatus.setBackgroundColor(Color.parseColor("#FFCDD2"))
            }
            "anggaran sudah ditentukan" -> {
                holder.tvStatus.setTextColor(Color.parseColor("#0D47A1"))
                holder.tvStatus.setBackgroundColor(Color.parseColor("#BBDEFB"))
            }
            "surat telah diterbitkan" -> {
                holder.tvStatus.setTextColor(Color.parseColor("#4A148C"))
                holder.tvStatus.setBackgroundColor(Color.parseColor("#E1BEE7"))
            }
            else -> {
                holder.tvStatus.setTextColor(Color.parseColor("#E65100"))
                holder.tvStatus.setBackgroundColor(Color.parseColor("#FFE0B2"))
            }
        }

        holder.itemView.setOnClickListener {
            val statusClean = item.status.lowercase().trim()
            if (statusClean == "diproses" || statusClean == "disetujui" || statusClean == "anggaran sudah ditentukan") {
                onItemClick?.invoke(item, kategoriSekarang)
            }
        }
    }

    override fun getItemCount(): Int = list.size

    private fun createSpannedText(context: Context, drawableRes: Int, text: String): SpannableStringBuilder {
        val builder = SpannableStringBuilder("   $text")
        val drawable = ContextCompat.getDrawable(context, drawableRes)

        drawable?.let {
            // Mengonversi ukuran icon ke 16dp
            val metrics = context.resources.displayMetrics
            val size = (16 * (metrics.densityDpi.toFloat() / 160f)).toInt()
            it.setBounds(0, 0, size, size)

            val imageSpan = ImageSpan(it, ImageSpan.ALIGN_BOTTOM)
            builder.setSpan(imageSpan, 1, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        return builder
    }
}