package com.sehatsentosa.pasien;

/**
 * ============================================================
 *  VALIDATOR KEAMANAN (Internal Security Validator)
 *  com.sehatsentosa.pasien — SAME package as RekamMedis
 * ============================================================
 *  This class is the proof that the (default) package-private design works:
 *  it can reach RekamMedis' hidden members simply by living in the same
 *  package, while no class in com.sehatsentosa.app or com.sehatsentosa.rawatinap
 *  can — not even the PasienVIP subclass.
 */
public class ValidatorKeamanan {

    /** Placeholder token factory — real systems would issue signed tokens. */
    public static String generasiTokenOtorisasi() {
        return "TOKEN-DOKTER-SAHIH";
    }

    /**
     * Validates a request using the server encryption key.
     * Calls the package-private ambilKunciEnkripsiServer() — legal here ONLY
     * because both classes share com.sehatsentosa.pasien.
     */
    public boolean validasiPermintaan(RekamMedis rekamMedis, String otorisasi) {
        String kunci = rekamMedis.ambilKunciEnkripsiServer();
        boolean valid = otorisasi != null
                && otorisasi.equals("TOKEN-DOKTER-SAHIH")
                && kunci != null && !kunci.isBlank();

        System.out.println(valid
                ? "[VALIDATOR] Permintaan divalidasi dengan kunci enkripsi server."
                : "[VALIDATOR] Validasi gagal.");
        return valid;
    }

    /** Internal audit: reads diagnosis through the package-private reader. */
    public void auditRiwayatDiagnosa(RekamMedis rekamMedis) {
        System.out.println("[AUDIT] Riwayat diagnosa (internal): "
                + rekamMedis.bacaRiwayatDiagnosaInternal());
    }
}
