package co.kr.pledo.lint

import com.android.tools.lint.detector.api.*
import com.intellij.psi.PsiMethod
import org.jetbrains.uast.UCallExpression

@Suppress("UnstableApiUsage")
class LoggerDetector : Detector(), SourceCodeScanner {

    override fun getApplicableMethodNames(): List<String> {
        return listOf("d", "i", "w", "e")
    }

    override fun visitMethodCall(context: JavaContext, node: UCallExpression, method: PsiMethod) {
        if (context.evaluator.isMemberInClass(method, "android.util.Log")) {
            val messageArgument: String
            if (node.valueArgumentCount == 2) {
                messageArgument = node.valueArguments[1].evaluate()?.toString() ?: ""
            } else return
            val methodName = node.methodName
            context.report(
                ActivityAffinityDetector.ACTIVITY_AFFINITY_ISSUE,
                node,
                context.getLocation(node),
                "애드벌룬 프로젝트는 CLog 클래스를 사용합니다.",
                LintFix.create()
                    .name("Change Log to CLog")
                    .replace()
                    .with("CLog.$methodName(\"$messageArgument\")")
                    .robot(true)
                    .independent(true)
                    .build()
            )
        }
    }

    companion object {
        val DEFAULT_LOGGER_ISSUE = Issue.create(
            "UseCLogForLogging",
            "android.util.Log detected",
            "애드벌룬 프로젝트는 CLog 클래스를 사용합니다.",
            Category.CUSTOM_LINT_CHECKS,
            7,
            Severity.WARNING,
            Implementation(LoggerDetector::class.java, Scope.JAVA_FILE_SCOPE)
        )
    }
}