package co.kr.pledo.lint

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.detector.api.Issue

@Suppress("UnstableApiUsage")
class AdballoonIssueRegistry : IssueRegistry() {
    override val issues: List<Issue>
        get() = listOf(
            ActivityAffinityDetector.ACTIVITY_AFFINITY_ISSUE,
            LoggerDetector.DEFAULT_LOGGER_ISSUE
        )
}