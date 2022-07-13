package co.kr.pledo.lint

import com.android.tools.lint.detector.api.*
import com.android.utils.forEach
import org.w3c.dom.Element

@Suppress("UnstableApiUsage")
class ActivityAffinityDetector : Detector(), Detector.XmlScanner {
    override fun getApplicableElements(): Collection<String> {
        return listOf(
            "activity"
        )
    }

    override fun visitElement(context: XmlContext, element: Element) {
        element.attributes?.forEach {
            if (it.nodeName.equals("android:taskAffinity"))
                return
        }
        context.report(
            ACTIVITY_AFFINITY_ISSUE,
            context.getNameLocation(element),
            "애드벌룬 프로젝트는 일반적인 앱의 흐름과 전화 상황에서의 흐름이 분리되어 있습니다.\n" +
                    "태스크를 온전히 분리하기 위해 모든 액티비티에 affinity 속성을 명시하는 것이 좋습니다.",
            LintFix.create()
                .name("Add taskAffinity")
                .set(null, "android:taskAffinity", "")
                .robot(true)
                .independent(true)
                .build()
        )
    }

    companion object {
        val ACTIVITY_AFFINITY_ISSUE = Issue.create(
            "ActivityAffinityMissing",
            "Missing affinity of activity",
            "Adballoon project must set taskAffinity property of activity",
            Category.CUSTOM_LINT_CHECKS,
            7,
            Severity.WARNING,
            Implementation(ActivityAffinityDetector::class.java, Scope.MANIFEST_SCOPE)
        )
    }
}