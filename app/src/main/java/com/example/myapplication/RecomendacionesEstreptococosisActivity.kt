package com.example.myapplication

import android.os.Bundle
import com.example.myapplication.databinding.ActivitySymptomDetailBaseBinding

class RecomendacionesEstreptococosisActivity : BaseSymptomDetailActivity() {
    override val title = "RECOMENDACIONES ESTREPTOCOCOSIS"
    override val description = ""
    override val type = "RECOMENDACIÓN"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySymptomDetailBaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.sectionTitle1.text = "Recomendaciones Generales"
        binding.sectionContent1.text = "• Se recomienda la aplicación de bacterinas contra estreptococo suis, en las hembras reproductoras y de reemplazo, para evitar brotes de la enfermedad en el hato reproductor.\n" +
                "• Se debe realizar la aplicación de bacterinas contra estreptococo suis, en lechones destetados, en explotaciones con alta incidencia de la enfermedad en destete y engorda.\n" +
                "• Lavar completamente a las cerdas antes de ingresarlas a las salas de parto.\n" +
                "• La infección con estreptococos se transmite a los lechones principalmente durante el parto, cuando pasa por el canal reproductivo.\n" +
                "• El brote de la infección se manifiesta entre la sexta semana de vida (destete) y la semana 16 (engorda) de vida de los cerdos.\n" +
                "• Mejorar la atención de partos, el secado de lechones, amarre y desinfección del ombligo, para evitar o reducir el contagio de estreptococos en los lechones."

        binding.sectionTitle2.text = "Ácido Yatrenico y Estimulante de Inmunidad"
        binding.sectionContent2.text = "ACIDO YATRENICO\nBIOS ESTIMULANTE DE LA INMUNIDAD\nSIGUE LAS RECOMENDACIONES DE USO DEL MEDICAMENTO DEL LABORATORIO FABRICANTE Y CONSULTE AL MÉDICO VETERINARIO\n\n23.4 MG   0.15 MG   3-5 DÍAS"

        binding.sectionTitle3.text = "Tratamiento Estreptococosis"
        binding.sectionContent3.text = "El tratamiento consiste principalmente en inyecciones intramusculares de larga acción ya que los cerdos enfermos comen y beben menos. El uso de antimicrobianos reduce la mortalidad y las lesiones por Estreptococo suis, pero no impide la infección. Los estreptococos son muy resistentes a diferentes antibióticos, pero son muy sensibles a los betalactámicos.\n\n" +
            "Tratamiento intramuscular (inyecciones):\n" +
            "Antibiótico: PENICILINA-ESTREPTOMICINA\n" +
            "Antibiótico: AMPICILINA\n" +
            "Desinflamatorio y analgésico: FLUMETASONA\n\n" +
            "Tratamiento en agua:\n" +
            "Antibiótico: AMOXICILINA (20MG x KG P.V.)\n" +
            "Durante 10 días.\n" +
            "Antipiréticos y analgésicos: METAMIZOL SÓDICO Y PARACETAMOL\n" +
            "Expectorantes y mucolíticos"

        // Puedes agregar más secciones y tarjetas si lo deseas, siguiendo el mismo patrón
    }
} 