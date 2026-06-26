package com.example.rekraprima;

import android.content.Context;
import android.graphics.Color;
import androidx.appcompat.app.AlertDialog;
import com.google.android.material.button.MaterialButton;

/**
 * Helper yang dipakai semua form activity untuk menangani mode manajer.
 * Panggil setup() di onCreate() setelah field sudah dinonaktifkan.
 */
public class ManagerModeHelper {

    public interface OnStatusChanged {
        void onChanged(String newStatus);
    }

    /**
     * @param context       Activity pemanggil
     * @param btnKirim      Tombol yang akan diubah jadi tombol Status
     * @param pengajuanId   ID pengajuan di DataStore
     * @param statusSaatIni Status saat ini ("Proses", "Diterima", "Ditolak")
     * @param callback      Dipanggil setelah status berubah (opsional, boleh null)
     */
    public static void setup(Context context, MaterialButton btnKirim,
                             String pengajuanId, String statusSaatIni,
                             OnStatusChanged callback) {

        switch (statusSaatIni) {
            case "Diterima":
                tampilkanStatusFinal(btnKirim, "Diterima", "#065F46", "#D1FAE5");
                break;

            case "Ditolak":
                tampilkanStatusFinal(btnKirim, "Ditolak", "#991B1B", "#FEE2E2");
                break;

            default: // "Proses" → manajer bisa memilih
                btnKirim.setText("Status");
                btnKirim.setEnabled(true);
                btnKirim.setAlpha(1f);
                btnKirim.setBackgroundTintList(
                        android.content.res.ColorStateList.valueOf(Color.parseColor("#F59E0B")));

                btnKirim.setOnClickListener(v -> {
                    new AlertDialog.Builder(context)
                            .setTitle("Ubah Status Pengajuan")
                            .setItems(new String[]{"Diterima", "Ditolak"}, (dialog, which) -> {
                                String pilihan = (which == 0) ? "Diterima" : "Ditolak";

                                // Simpan ke DataStore
                                DataStore.getInstance().updateStatus(pengajuanId, pilihan);

                                // Update tampilan tombol
                                if (pilihan.equals("Diterima")) {
                                    tampilkanStatusFinal(btnKirim, "Diterima", "#065F46", "#D1FAE5");
                                } else {
                                    tampilkanStatusFinal(btnKirim, "Ditolak", "#991B1B", "#FEE2E2");
                                }

                                if (callback != null) callback.onChanged(pilihan);
                            })
                            .show();
                });
                break;
        }
    }

    /** Tampilkan tombol status final (tidak bisa ditekan lagi) */
    private static void tampilkanStatusFinal(MaterialButton btn,
                                             String teks, String textColor, String bgColor) {
        btn.setText(teks);
        btn.setEnabled(false);
        btn.setAlpha(1f); // tetap terlihat jelas meski disabled
        btn.setTextColor(Color.parseColor(textColor));
        btn.setBackgroundTintList(
                android.content.res.ColorStateList.valueOf(Color.parseColor(bgColor)));
    }
}