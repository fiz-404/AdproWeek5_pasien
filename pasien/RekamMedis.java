package com.sehatsentosa.pasien;

/**
 * ============================================================
 *  REKAM MEDIS (Medical Record) — com.sehatsentosa.pasien
 * ============================================================
 *  Encapsulation strategy: every FIELD gets the STRICTEST modifier that still
 *  satisfies its business rule, then methods are layered on top to open exactly
 *  the amount of access required — no more.
 *
 *  Java access matrix reminder (most -> least restrictive):
 *  private < (default/package-private) < protected < public
 *
 *  | Member                     | Modifier      | Why                                      |
 *  |----------------------------|---------------|------------------------------------------|
 *  | nomorRekamMedis            | private final | read-only for the world, immutable       |
 *  | riwayatDiagnosaPenyakit    | private       | confidential, gated by validation        |
 *  | kataSandiPortalMedis       | private       | WRITE-ONLY: no getter may ever exist     |
 *  | persentaseDiskonLayanan    | protected     | readable/writable by the VIP subclass    |
 *  | kunciEnkripsiServer        | (default)     | package-private: validator only          |
 *  | cetakRingkasanMedis()      | public        | cashier + registration need it           |
 */
public class RekamMedis {

    // ------------------------------------------------------------------
    // 1. NOMOR REKAM MEDIS (Medical Record Number)
    //    Business rule: readable by the ENTIRE system, but PERMANENT.
    //    -> private + final : no one can reassign the field itself.
    //    -> Exposure happens ONLY through a public getter (read-only property).
    //    -> There is intentionally NO setter: immutability is enforced by design.
    // ------------------------------------------------------------------
    private final String nomorRekamMedis;

    // ------------------------------------------------------------------
    // 2. RIWAYAT DIAGNOSA PENYAKIT (Diagnosis History)
    //    Business rule: highly confidential; never accessible directly.
    //    -> private : invisible to every other class, even subclasses.
    //    -> Access is granted ONLY through validated public procedures below
    //       (the "official procedures" of the scenario).
    // ------------------------------------------------------------------
    private String riwayatDiagnosaPenyakit;

    // ------------------------------------------------------------------
    // 3. KATA SANDI PORTAL MEDIS (Portal Password)
    //    Business rule: patients may UPDATE it, but the system must offer
    //    NO way whatsoever to view/read the raw value.
    //    -> private + WRITE-ONLY: exactly one setter exists below and
    //       deliberately NO getter. A getKataSandiPortalMedis() method would
    //       violate the rule, so it simply does not exist in this class.
    // ------------------------------------------------------------------
    private String kataSandiPortalMedis;

    // ------------------------------------------------------------------
    // 4. PERSENTASE DISKON LAYANAN (VIP Service Discount)
    //    Business rule: the VIP subclass in com.sehatsentosa.rawatinap must
    //    be able to READ and MODIFY it DIRECTLY based on room category.
    //    -> protected : visible to subclasses even across packages.
    //       private would lock PasienVIP out; public would leak it to the
    //       app package. protected is the precise fit.
    // ------------------------------------------------------------------
    protected double persentaseDiskonLayanan;

    // ------------------------------------------------------------------
    // 5. KUNCI ENKRIPSI SERVER (Server Encryption Key)
    //    Business rule: usable by the internal validator in THIS package,
    //    completely hidden from com.sehatsentosa.app and .rawatinap.
    //    -> (default) package-private : no modifier keyword at all.
    //       Only classes inside com.sehatsentosa.pasien can see it;
    //       subclasses in other packages CANNOT see it (unlike protected).
    // ------------------------------------------------------------------
    String kunciEnkripsiServer;

    /**
     * Constructs a fully-initialised medical record.
     *
     * @param nomorRekamMedis         permanent medical record number
     * @param riwayatDiagnosaPenyakit initial diagnosis history
     * @param kataSandiPortalMedis    initial portal password
     * @param kunciEnkripsiServer     server encryption key (package-internal)
     */
    public RekamMedis(String nomorRekamMedis,
                      String riwayatDiagnosaPenyakit,
                      String kataSandiPortalMedis,
                      String kunciEnkripsiServer) {
        this.nomorRekamMedis = nomorRekamMedis;
        this.riwayatDiagnosaPenyakit = riwayatDiagnosaPenyakit;
        this.kataSandiPortalMedis = kataSandiPortalMedis;
        this.kunciEnkripsiServer = kunciEnkripsiServer;
        this.persentaseDiskonLayanan = 0.0; // regular patients start with no discount
    }

    // ==================================================================
    // READ-ONLY PROPERTY — Nomor Rekam Medis
    // public getter so the WHOLE system can verify identity; the final
    // field guarantees the value can never change after instantiation.
    // ==================================================================
    public String getNomorRekamMedis() {
        return nomorRekamMedis;
    }

    // ==================================================================
    // VALIDATED OFFICIAL PROCEDURES — Riwayat Diagnosa Penyakit
    // These public methods are the ONLY doors to the confidential field.
    // Both funnel through one private validation helper (DRY principle),
    // so the security policy lives in exactly one place.
    // ==================================================================

    /** Official read procedure: only a properly authorised session may read. */
    public String bacaRiwayatDiagnosa(String otorisasi) {
        if (!validasiOtorisasi(otorisasi)) {
            System.out.println("[DITOLAK] Otorisasi tidak valid — riwayat diagnosa tidak dibuka.");
            return null;
        }
        System.out.println("[OKE] Riwayat diagnosa dibaca dengan otorisasi resmi.");
        return riwayatDiagnosaPenyakit;
    }

    /** Official update procedure: only a properly authorised session may write. */
    public void perbaruiRiwayatDiagnosa(String diagnosaBaru, String otorisasi) {
        if (!validasiOtorisasi(otorisasi)) {
            System.out.println("[DITOLAK] Otorisasi tidak valid — riwayat diagnosa tidak diubah.");
            return;
        }
        if (diagnosaBaru == null || diagnosaBaru.isBlank()) {
            System.out.println("[DITOLAK] Diagnosa baru tidak boleh kosong.");
            return;
        }
        this.riwayatDiagnosaPenyakit = diagnosaBaru;
        System.out.println("[OKE] Riwayat diagnosa berhasil diperbarui.");
    }

    /** Single point of validation (placeholder logic — swap for real auth later). */
    private boolean validasiOtorisasi(String otorisasi) {
        return otorisasi != null && otorisasi.equals("TOKEN-DOKTER-SAHIH");
    }

    // ==================================================================
    // WRITE-ONLY PROPERTY — Kata Sandi Portal Medis
    // A public setter with placeholder validation (min 6 characters).
    // NOTE: there is NO getKataSandiPortalMedis() anywhere in this class —
    // that omission IS the encapsulation guarantee required by the rule.
    // ==================================================================
    public void aturKataSandiPortalMedis(String kataSandiBaru) {
        if (kataSandiBaru == null || kataSandiBaru.length() < 6) {
            System.out.println("[DITOLAK] Kata sandi baru minimal 6 karakter.");
            return;
        }
        this.kataSandiPortalMedis = kataSandiBaru;
        System.out.println("[OKE] Kata sandi portal medis berhasil diperbarui.");
    }

    // ==================================================================
    // PACKAGE-INTERNAL ACCESS — Kunci Enkripsi Server
    // No modifier on purpose: package-private, so ONLY ValidatorKeamanan
    // (same package) can call this. The app and rawatinap packages see
    // neither the field nor this method.
    // ==================================================================
    String ambilKunciEnkripsiServer() {
        return kunciEnkripsiServer;
    }

    /** Package-private internal diagnosis read for the validator's audit trail. */
    String bacaRiwayatDiagnosaInternal() {
        return riwayatDiagnosaPenyakit;
    }

    // ==================================================================
    // PROTECTED DISCOUNT ACCESSORS
    // Optional convenience layer over the protected field: keeps the door
    // open for PasienVIP while still letting us add checks here later.
    // ==================================================================
    protected double getPersentaseDiskonLayanan() {
        return persentaseDiskonLayanan;
    }

    protected void aturPersentaseDiskonLayanan(double persentase) {
        if (persentase < 0.0 || persentase > 100.0) {
            System.out.println("[DITOLAK] Diskon harus di antara 0% dan 100%.");
            return;
        }
        this.persentaseDiskonLayanan = persentase;
    }

    // ==================================================================
    // PUBLIC PRINT SUMMARY — Fungsi Pencetakan Ringkasan Medis
    // Business rule: freely accessible to cashier & registration services
    // in com.sehatsentosa.app, hence public. It only prints NON-sensitive
    // fields, so making it public is safe.
    // ==================================================================
    public void cetakRingkasanMedis() {
        System.out.println("---------- RINGKASAN MEDIS ----------");
        System.out.println("No. Rekam Medis : " + nomorRekamMedis);
        System.out.println("Diskon Layanan  : " + persentaseDiskonLayanan + "%");
        System.out.println("-------------------------------------");
    }
}
