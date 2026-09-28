package com.sehatsentosa.app;

import com.sehatsentosa.pasien.RekamMedis;
import com.sehatsentosa.pasien.ValidatorKeamanan;
import com.sehatsentosa.rawatinap.PasienVIP;

/**
 * ============================================================
 *  APLIKASI RUMAH SAKIT — com.sehatsentosa.app
 * ============================================================
 *  Registration & cashier services. This package is "outside" everything,
 *  so it only sees: public members, and nothing else. Each section below
 *  demonstrates one business rule in action.
 */
public class AplikasiRumahSakit {

    public static void main(String[] args) {
        System.out.println("=== PENDAFTARAN PASIEN ===");
        RekamMedis rekam = new RekamMedis(
                "RM-2026-0001",
                "Demam berdarah",
                "sandilama123",
                "KUNCI-RAHASIA-SERVER");

        // RULE 1: record number is publicly READABLE...
        System.out.println("No. rekam medis: " + rekam.getNomorRekamMedis());
        // ...but PERMANENT. Uncommenting the next line would not compile:
        // rekam.nomorRekamMedis = "RM-HACKED";   // private final — no setter exists

        // RULE 6: print summary is PUBLIC — cashier/registration call it freely.
        rekam.cetakRingkasanMedis();

        System.out.println("\n=== KASIR: LAYANAN VALIDASI ===");
        ValidatorKeamanan validator = new ValidatorKeamanan();

        // Official diagnosis procedure with a WRONG token -> refused.
        rekam.perbaruiRiwayatDiagnosa("Tifoid", "TOKEN-PALSU");

        // Official procedure with a REAL token -> accepted.
        String token = ValidatorKeamanan.generasiTokenOtorisasi();
        rekam.perbaruiRiwayatDiagnosa("Tifoid", token);
        rekam.bacaRiwayatDiagnosa(token);

        // Direct access to the confidential field is impossible — uncommenting
        // either line below would fail to compile:
        // System.out.println(rekam.riwayatDiagnosaPenyakit); // private
        // rekam.riwayatDiagnosaPenyakit = "Sehat";           // private

        System.out.println("\n=== PASIEN: GANTI KATA SANDI ===");
        rekam.aturKataSandiPortalMedis("sandiBaru456"); // write-only: works
        rekam.aturKataSandiPortalMedis("123");          // placeholder validation refuses

        // There is NO getter for the password anywhere in the API — uncommenting
        // would not compile:
        // rekam.getKataSandiPortalMedis(); // method does not exist (write-only)

        System.out.println("\n=== PASIEN VIP (rawatinap) ===");
        PasienVIP vip = new PasienVIP("RM-2026-0002", "Migrain", "vip12345", "VIP Suite");
        vip.cetakRingkasanMedis();

        // VIP discount: protected member, modified from the SUBCLASS. Allowed.
        vip.pindahKamar("Suite");

        // Protected members are NOT visible to non-subclasses in this package.
        // Uncommenting would not compile from AplikasiRumahSakit:
        // rekam.persentaseDiskonLayanan = 99.0;   // protected, non-subclass access

        // Encryption key: completely hidden from the app package.
        // Uncommenting would not compile:
        // System.out.println(rekam.kunciEnkripsiServer);        // package-private
        // rekam.ambilKunciEnkripsiServer();                     // package-private

        // The validator CAN audit internally (same package as RekamMedis).
        validator.auditRiwayatDiagnosa(rekam);
        System.out.println("Token valid? " + validator.validasiPermintaan(rekam, token));
    }
}
