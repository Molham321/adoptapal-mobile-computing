package de.fhe.adoptapal.core

import de.fhe.adoptapal.repository.AnimalCategoryRepository
import de.fhe.adoptapal.repository.AnimalRepository
import de.fhe.adoptapal.repository.ColorRepository
import io.quarkus.runtime.StartupEvent
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.event.Observes
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import java.time.LocalDate

/**
 * Application-scoped class responsible for initializing the application during startup.
 *
 * @property animalRepository The repository for managing animal entities.
 * @property animalCategoryRepository The repository for managing animal category entities.
 * @property colorRepository The repository for managing color entities.
 */
@ApplicationScoped
class Startup {
    @Inject
    lateinit var animalRepository: AnimalRepository

    @Inject
    lateinit var animalCategoryRepository: AnimalCategoryRepository

    @Inject
    lateinit var colorRepository: ColorRepository

    /**
     * Transactional method that initializes the application during startup.
     *
     * Deletes existing animal entities, adds color and category entities, and adds sample animal entities.
     *
     * @param evt The observed startup event.
     */
    @Transactional
    fun loadAnimals(@Observes evt: StartupEvent?) {

        animalRepository.let {
            it.deleteAll()

            val color1 = colorRepository.add("Weiß")
            val color2 = colorRepository.add("Schwarz")
            val color3 = colorRepository.add("Rot")
            val color4 = colorRepository.add("Grün")
            val color5 = colorRepository.add("Orange")
            val color6 = colorRepository.add("Gelb")
            val color7 = colorRepository.add("Braun")
            val color8 = colorRepository.add("Grau")
            val color9 = colorRepository.add("Blau")
            val color10 = colorRepository.add("Mehrfarbig")
            val color11 = colorRepository.add("Andere")

            val category1 = animalCategoryRepository.add("Katze")
            val category2 = animalCategoryRepository.add("Hund")
            val category3 = animalCategoryRepository.add("Fisch")
            val category4 = animalCategoryRepository.add("Reptil")
            val category5 = animalCategoryRepository.add("Nagetier")
            val category6 = animalCategoryRepository.add("Vogel")
            val category7 = animalCategoryRepository.add("Andere")

            it.add(
                "Albert",
                "Albert die schwarze Katze",
                color2.id,
                true,
                category1.id,
                LocalDate.of(2018, 7, 30),
                2.5f,
                1,
                1
            )

            it.add(
                "Daisy",
                "Daisy die freundliche Dame",
                color7.id,
                false,
                category2.id,
                LocalDate.of(2020, 12, 9),
                4.8f,
                1,
                2
            )
        }
    }
}