import os
import json
import xml.etree.ElementTree as ET
import glob
from jinja2 import Environment, FileSystemLoader

# ── Parse JUnit XML for failed tests ──────────────────────────────────────────
def parse_failed_tests():
    lines = []
    for report in glob.glob("**/TEST-*.xml", recursive=True):
        tree = ET.parse(report)
        root = tree.getroot()
        classname = root.attrib.get("name", "UnknownTest")

        for tc in root.findall("testcase"):
            failure = tc.find("failure")
            error   = tc.find("error")
            node    = failure if failure is not None else error
            if node is None:
                continue

            test_name    = tc.attrib.get("name", "unknownTest")
            raw          = (node.text or "").strip()
            raw_lines    = raw.splitlines()

            expected, but_was, location, full_reason = "N/A", "N/A", "N/A", "N/A"

            for raw_line in raw_lines:
                l = raw_line.strip()
                if l.startswith("expected:"):
                    expected = l.replace("expected:", "").strip().strip("<>")
                elif l.startswith("but was:"):
                    but_was = l.replace("but was:", "").strip().strip("<>")
                elif "(" in l and ".java:" in l and location == "N/A":
                    location = l

            if raw_lines:
                full_reason = raw_lines[0].strip()

            lines.append({
                "classname":   classname,
                "test_name":   test_name,
                "full_reason": full_reason,
                "expected":    expected,
                "but_was":     but_was,
                "location":    location,
            })

    return lines

# ── Format failed tests as plain text block ────────────────────────────────────
def format_failed_tests(failed_tests):
    lines = []
    for test in failed_tests:
        lines.append(
            f"[FAIL] {test['classname']} :: {test['test_name']}\n"
            f"       Reason  : {test['full_reason']}\n"
            f"       expected: <{test['expected']}>\n"
            f"       but was : <{test['but_was']}>\n"
            f"       Location: {test['location']}"
        )
    return "\n\n".join(lines) if lines else ""

# ── Load values from environment ───────────────────────────────────────────────
status        = os.environ.get("STATUS", "failure")
tests_total   = os.environ.get("TESTS_TOTAL", "N/A")
tests_passed  = os.environ.get("TESTS_PASSED", "N/A")
tests_failed  = os.environ.get("TESTS_FAILED", "N/A")
tests_skipped = os.environ.get("TESTS_SKIPPED", "N/A")
repository    = os.environ.get("REPOSITORY", "N/A")
branch        = os.environ.get("BRANCH", "N/A")
run_number    = os.environ.get("RUN_NUMBER", "N/A")
commit        = os.environ.get("COMMIT", "N/A")
run_url       = os.environ.get("RUN_URL", "#")

if status == "success":
    status_icon  = "✅"
    status_color = "#28a745"
    status_label = "PASSED"
else:
    status_icon  = "❌"
    status_color = "#dc3545"
    status_label = "FAILED"

failed_tests       = parse_failed_tests() if status == "failure" else []
failed_tests_block = format_failed_tests(failed_tests)

# ── Render Jinja2 template ─────────────────────────────────────────────────────
template_dir = os.path.join(os.getcwd(), ".github", "templates")
env          = Environment(FileSystemLoader(template_dir))
template     = env.get_template("slack.jinja")

payload = template.render(
    status             = status,
    status_icon        = status_icon,
    status_color       = status_color,
    status_label       = status_label,
    repository         = repository,
    branch             = branch,
    run_number         = run_number,
    commit             = commit,
    run_url            = run_url,
    tests_total        = tests_total,
    tests_passed       = tests_passed,
    tests_failed       = tests_failed,
    tests_skipped      = tests_skipped,
    failed_tests_block = failed_tests_block,
)

# Validate it is proper JSON before writing
parsed = json.loads(payload)
with open("/tmp/slack_payload.json", "w") as f:
    json.dump(parsed, f, indent=2)

print("Slack payload rendered successfully.")