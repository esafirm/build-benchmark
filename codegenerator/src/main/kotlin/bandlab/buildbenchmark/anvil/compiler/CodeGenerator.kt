package bandlab.buildbenchmark.anvil.compiler

import bandlab.buildbenchmark.CustomAnnotation
import com.google.auto.service.AutoService
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration

class CodeGenerator(
    private val env: SymbolProcessorEnvironment,
) : SymbolProcessor {

    @AutoService(SymbolProcessorProvider::class)
    class Provider : SymbolProcessorProvider {
        override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
            environment.logger.info("Loading ConfigSelector code gen provider")
            return CodeGenerator(environment)
        }
    }

    override fun process(resolver: Resolver): List<KSAnnotated> {
        resolver.getSymbolsWithAnnotation(CustomAnnotation::class.qualifiedName!!)
            .filterIsInstance<KSClassDeclaration>()
            .forEach {
                env.logger.info("> Process ${it.qualifiedName}")
            }
        return emptyList()
    }
}
