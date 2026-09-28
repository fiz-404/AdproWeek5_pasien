package com.sehatsentosa.rawatinap;

import com.sehatsentosa.pasien.RekamMedis;

/**
 * ============================================================
 *  PASIEN VIP (VIP Patient) — com.sehatsentosa.rawatinap
 * ============================================================
 *  A SUBCLASS in a DIFFERENT package. This is the textbook 'protected'
 *  scenario: PasienVIP may read/write RekamMedis' protected members,
 *  but unrelated classes in rawatinap or app may NOT.
 */
public class PasienVIP extends RekamMedis {

    private String kategoriKamar;

    public PasienVIP(String nomorRekamMedis,
                     String riwayatDiagnosaPenyakit,
                     String kataSandiPortalMedis,
                     String kategoriKamar) {
        // NOTE: we pass "" for the encryption key. The subclass has no way to
        // obtain the real key — package-private members are invisible here.
        super(nomorRekamMedis, riwayatDiagnosaPenyakit, kataSandiPortalMedis, "");
        this.kategoriKamar = kategoriKamar;
        terapkanDiskonKategori();
    }

    /**
     * DIRECT access to the protected field persentaseDiskonLayanan,
     * exactly as the business rule demands. This line would NOT compile
     * if the field were private or package-private.
     */
    public void terapkanDiskonKategori() {
        switch (kategoriKamar) {
            case "VIP Suite" -> this.persentaseDiskonLayanan = 30.0;
            case "Suite" -> this.persentaseDiskonLayanan = 20.0;
            default -> this.persentaseDiskonLayanan = 10.0;
        }
        System.out.println("[VIP] Diskon " + kategoriKamar
                + " -> " + persentaseDiskonLayanan + "%");
    }

    public String getKategoriKamar() {
        return kategoriKamar;
    }

    /**
     * Guarded change of room category; recalculates the discount via the
     * protected setter (which validates the 0-100 range).
     */
    public void pindahKamar(String kategoriBaru) {
        this.kategoriKamar = kategoriBaru;
        terapkanDiskonKategori();
    }
}
