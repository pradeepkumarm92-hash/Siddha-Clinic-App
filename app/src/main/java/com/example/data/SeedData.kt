package com.example.data

import com.example.data.model.ClinicSettings
import com.example.data.model.Medicine
import com.example.data.model.Patient

object SeedData {
    val initialMedicines = listOf(
        Medicine(
            name = "Amukkara Choornam",
            category = "Choornam",
            stockQuantity = 50,
            unit = "Packs",
            pricePerUnit = 60.0,
            defaultDosage = "1-0-1 (1 Tsp)",
            defaultAnupanam = "Milk",
            timing = "After Food",
            description = "For stress, physical weakness, insomnia & nervous exhaustion."
        ),
        Medicine(
            name = "Nilavembu Kudineer",
            category = "Kudineer / Kashayam",
            stockQuantity = 40,
            unit = "Packs",
            pricePerUnit = 45.0,
            defaultDosage = "30ml Twice",
            defaultAnupanam = "Warm Water",
            timing = "Before Food",
            description = "Classical Siddha herbal formulation for fever & immunity."
        ),
        Medicine(
            name = "Mahaeladi Thailam",
            category = "Thailam",
            stockQuantity = 25,
            unit = "Bottles",
            pricePerUnit = 180.0,
            defaultDosage = "External Application",
            defaultAnupanam = "Warm Water",
            timing = "Before Bath",
            description = "Cooling herbal oil for skin conditions, headache & body heat."
        ),
        Medicine(
            name = "Thiriphala Choornam",
            category = "Choornam",
            stockQuantity = 60,
            unit = "Packs",
            pricePerUnit = 50.0,
            defaultDosage = "1 Tsp",
            defaultAnupanam = "Warm Water",
            timing = "At Bedtime",
            description = "Digestive tonic, detoxifier & natural bowel regulator."
        ),
        Medicine(
            name = "Kabasura Kudineer",
            category = "Kudineer / Kashayam",
            stockQuantity = 35,
            unit = "Packs",
            pricePerUnit = 50.0,
            defaultDosage = "30ml Twice",
            defaultAnupanam = "Hot Water",
            timing = "Before Food",
            description = "Siddha polyherbal remedy for respiratory symptoms, cough & cold."
        ),
        Medicine(
            name = "Bramhi Leghyam",
            category = "Leghyam",
            stockQuantity = 20,
            unit = "Jars",
            pricePerUnit = 220.0,
            defaultDosage = "5 Grams Twice",
            defaultAnupanam = "Warm Milk",
            timing = "After Food",
            description = "Memory booster, brain tonic & mental rejuvenator."
        ),
        Medicine(
            name = "Nandi Cheendhooram",
            category = "Parpam & Cheendhooram",
            stockQuantity = 15,
            unit = "Containers",
            pricePerUnit = 250.0,
            defaultDosage = "100 mg",
            defaultAnupanam = "Honey",
            timing = "After Food",
            description = "Mineral-herb Siddha preparation for chronic skin issues & joint pain."
        ),
        Medicine(
            name = "Vallarai Tablet",
            category = "Tablet / Vati",
            stockQuantity = 100,
            unit = "Strip (10 Tab)",
            pricePerUnit = 35.0,
            defaultDosage = "1-0-1",
            defaultAnupanam = "Warm Water",
            timing = "After Food",
            description = "Centella asiatica formulation for mental clarity & circulation."
        ),
        Medicine(
            name = "Adathodai Manappagu",
            category = "Syrup / Manappagu",
            stockQuantity = 25,
            unit = "Bottles",
            pricePerUnit = 110.0,
            defaultDosage = "10ml Twice",
            defaultAnupanam = "Warm Water",
            timing = "After Food",
            description = "Herbal cough syrup for asthma, bronchitis & dry cough."
        ),
        Medicine(
            name = "Karisalai Ghritham",
            category = "Leghyam",
            stockQuantity = 18,
            unit = "Jars",
            pricePerUnit = 240.0,
            defaultDosage = "5 Grams",
            defaultAnupanam = "Warm Milk",
            timing = "Before Food",
            description = "Classical liver tonic & iron rejuvenator."
        ),
        Medicine(
            name = "Trikatu Choornam",
            category = "Choornam",
            stockQuantity = 45,
            unit = "Packs",
            pricePerUnit = 55.0,
            defaultDosage = "1-0-1 (Half Tsp)",
            defaultAnupanam = "Honey",
            timing = "After Food",
            description = "Chukku, Milagu & Thippili blend for digestion & congestion."
        ),
        Medicine(
            name = "Muthu Parpam",
            category = "Parpam & Cheendhooram",
            stockQuantity = 12,
            unit = "Containers",
            pricePerUnit = 350.0,
            defaultDosage = "50 mg",
            defaultAnupanam = "Milk / Ghee",
            timing = "After Food",
            description = "Calcinated pearl for acidity, ulcers & body heat reduction."
        )
    )

    val initialPatients = listOf(
        Patient(
            name = "K. Ramanathan",
            age = 48,
            gender = "Male",
            phone = "98401 12345",
            address = "No 12, Main Street, Chennai, TN",
            nadiDiagnostics = "Pitham Elevated (Body Heat, Acidity)",
            chiefComplaints = "Gastric irritation, indigestion & mild insomnia",
            medicalHistory = "Hypertension under control"
        ),
        Patient(
            name = "S. Meenakshi",
            age = 56,
            gender = "Female",
            phone = "94442 67890",
            address = "West Car Street, Madurai, TN",
            nadiDiagnostics = "Vatham Elevated (Joint Stiffness)",
            chiefComplaints = "Knee joint pain, morning stiffness & lower back ache",
            medicalHistory = "No known allergies"
        ),
        Patient(
            name = "M. Vijay",
            age = 32,
            gender = "Male",
            phone = "97890 54321",
            address = "Avinashi Road, Coimbatore, TN",
            nadiDiagnostics = "Kapham Dominant (Respiratory Congestion)",
            chiefComplaints = "Recurrent cough, sinus heaviness & allergic rhinitis",
            medicalHistory = "Asthma history in winter"
        )
    )

    val defaultSettings = ClinicSettings(
        id = 1,
        clinicName = "Sri Siddha Herbal Healthcare Clinic",
        tagline = "Traditional Siddha Medicine & Nadi Healing Center",
        vaidyarName = "Dr. S. Vaidyanathan B.S.M.S.",
        registrationNumber = "REG-BSMS-48921",
        phone = "+91 98765 43210",
        address = "No. 42, Temple Road, Herbal Nagar, Tamil Nadu",
        defaultConsultationFee = 200.0,
        gstTaxPercentage = 0.0,
        customFooterMessage = "Pathiya Unavu (Dietary Discipline) is essential for Siddha medicine efficacy. Take prescribed Anupanam regularly."
    )
}
