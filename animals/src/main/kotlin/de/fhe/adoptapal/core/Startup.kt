package de.fhe.adoptapal.core

import de.fhe.adoptapal.repository.CategoryRepository
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
 * @property categoryRepository The repository for managing animal category entities.
 * @property colorRepository The repository for managing color entities.
 */
@ApplicationScoped
class Startup {
    @Inject
    lateinit var animalRepository: AnimalRepository

    @Inject
    lateinit var categoryRepository: CategoryRepository

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

            colorRepository.create("Weiß")
            val color2 = colorRepository.create("Schwarz")
            colorRepository.create("Rot")
            colorRepository.create("Grün")
            colorRepository.create("Orange")
            colorRepository.create("Gelb")
            val color7 = colorRepository.create("Braun")
            colorRepository.create("Grau")
            colorRepository.create("Blau")
            colorRepository.create("Mehrfarbig")
            colorRepository.create("Andere")

            val category1 = categoryRepository.create("Katze")
            val category2 = categoryRepository.create("Hund")
            categoryRepository.create("Fisch")
            categoryRepository.create("Reptil")
            categoryRepository.create("Nagetier")
            categoryRepository.create("Vogel")
            categoryRepository.create("Andere")

            it.create(
                "Albert",
                "Albert die schwarze Katze",
                color2.id!!,
                true,
                category1.id!!,
                LocalDate.of(2018, 7, 30),
                2.5f,
                1,
                1
            )

            it.create(
                "Daisy",
                "Daisy die freundliche Dame",
                color7.id!!,
                false,
                category2.id!!,
                LocalDate.of(2020, 12, 9),
                4.8f,
                1,
                2
            )
        }
    }
}