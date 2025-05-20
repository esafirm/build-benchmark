package bandlab.buildbenchmark.anvil.compiler

import bandlab.buildbenchmark.CustomAnnotation
import bandlab.buildbenchmark.GenerateInjector
import com.google.auto.service.AutoService
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.anvil.annotations.MergeComponent
import com.squareup.anvil.compiler.internal.createAnvilSpec
import com.squareup.anvil.compiler.internal.ksp.fqName
import com.squareup.anvil.compiler.internal.ksp.isInterface
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.STAR
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asTypeName
import com.squareup.kotlinpoet.ksp.kspDependencies
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.writeTo
import dagger.Module
import dagger.Provides
import kotlin.sequences.forEach

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
        resolver.resolveCustomAnnotation()
        resolver.resolveInjectorGenerator()
        return emptyList()
    }

    private fun Resolver.resolveInjectorGenerator() {
        getSymbolsWithAnnotation(GenerateInjector::class.qualifiedName!!)
            .filterIsInstance<KSClassDeclaration>()
            .forEach { target ->
                env.logger.info("[CodeGenerator] Process ${target.qualifiedName?.asString()}")

                if (!target.isInterface()) {
                    error("The target injector must be an interface")
                }

                val clazz = target.toClassName()
                val componentName = "${target.simpleName.asString()}Injector"
                val componentPackage = target.packageName.asString()

                val fileSpec = FileSpec.createAnvilSpec(
                    packageName = componentPackage,
                    fileName = componentName
                ) {
                    val genericModuleName = addGenericModule(target)
                    val genericModule = genericModuleName?.let { ClassName(componentPackage, it) }

                    TypeSpec.interfaceBuilder(componentName)
                        .addAnnotation(
                            AnnotationSpec.builder(MergeComponent::class)
                                .addMember("scope = %T::class", clazz)
                                .addMember("modules=[%T::class]", genericModule!!)
                                .build()
                        )
                        .build()
                        .apply(::addType)
                }

                fileSpec.writeTo(
                    codeGenerator = env.codeGenerator,
                    dependencies = fileSpec.kspDependencies(
                        aggregating = false,
                        originatingKSFiles = listOf(
                            target.containingFile ?: error("$target does not have containing file")
                        )
                    ),
                )
            }
    }

    private fun Resolver.resolveCustomAnnotation() {
        getSymbolsWithAnnotation(CustomAnnotation::class.qualifiedName!!)
            .filterIsInstance<KSClassDeclaration>()
            .forEach {

                env.logger.info(
                    """
                    [CodeGenerator] Process
                    
                    toString: $it
                    qualified: ${it.qualifiedName?.asString()}
                    simple: ${it.simpleName.asString()}
                    fqName: ${it.fqName}
                """.trimIndent()
                )

                val componentName = "${it.simpleName.asString()}Impl"
                val fileSpec = FileSpec.createAnvilSpec(
                    packageName = it.packageName.asString(),
                    fileName = componentName
                ) {
                    TypeSpec.objectBuilder(componentName)
                        .addFunction(
                            FunSpec.builder("log")
                                .addCode("println(\"\$this\")")
                                .build()
                        )
                        .build()
                        .apply(::addType)
                }

                fileSpec.writeTo(
                    codeGenerator = env.codeGenerator,
                    dependencies = fileSpec.kspDependencies(
                        aggregating = false,
                        originatingKSFiles = listOf(
                            it.containingFile ?: error("$it does not have containing file")
                        )
                    ),
                )
            }
    }

    internal fun FileSpec.Builder.addGenericModule(target: KSClassDeclaration): String? {
        val moduleName = "${target.simpleName.asString()}GenericModule"
        val genericTypeName = target.simpleName.asString()
        val paramName = genericTypeName.replaceFirstChar { it.lowercase() }

        TypeSpec.objectBuilder(moduleName)
            .addAnnotation(Module::class)
            .addFunction(
                FunSpec.builder("provide$genericTypeName")
                    .addAnnotation(Provides::class)
                    .addParameter(paramName, target.toClassName())
                    .returns(Any::class.asTypeName())
                    .addCode("return %L", paramName)
                    .build()
            )
            .build()
            .apply(::addType)

        return moduleName
    }
}
