# Chrono

**Chrono**: a productivity timer & task tracker

Description

- Chrono is a small JavaFX application that helps users to manage their tasks, run a countdown timer, and switch themes/store items. It was developed in IntelliJ and uses JavaFX, SceneBuilder, and lastly Maven for build/dependency management.

Design/Figma

- Prototype & assets: https://www.figma.com/design/5vsupr5xMDlI5zN123XdFc/Chrono?node-id=0-1&t=6UpPxw3BZN2QKJ0g-1

Clockwork Productions Members

- `Collin Scheibel`
- `Fabian Figueroa`
- `Davis Howe`
- `Matthew Eggers`

Prerequisites

- JDK (project `main` targets Java 24). Use the JDK you have downloaded and run the project from IntelliJ, or install a matching JDK for CLI use.
- Internet access the first time to download Maven/JavaFX dependencies.

Run (IntelliJ is recommended)

1. Open IntelliJ and choose `File → Open`; select the project root (the folder with `pom.xml`).
2. If IntelliJ prompts to import as a Maven project you accept it.
3. Set Project SDK (File → Project Structure → Project SDK) to an installed JDK (wherever you have it installed).
4. Run the main class `edu.utsa.cs3443.chrono.ChronoApplication` or use the Maven `javafx:run` (this can be a good workaround if having issues with the main class method).

IntelliJ run configuration (JavaFX details)

- If you run the application directly from IntelliJ you will need to add JavaFX VM options so the Java runtime can find the JavaFX modules.
- Create or edit a Run Configuration for the main class `edu.utsa.cs3443.chrono.ChronoApplication` and set:

  - **Project SDK**: your installed JDK (match what's in your `pom.xml` if possible).
  - **Working directory**: project root (e.g., `C:\path\to\project\Chrono`).
  - **VM options**: add a `--module-path` that points to the JavaFX SDK `lib` folder and `--add-modules` for the JavaFX modules used.
    > *double-check `;` or `:` in the path following `--module-path`*:

  Example (Windows)

  ```text
  --module-path "C:\path\to\jdk\lib;target\classes" --add-modules javafx.controls,javafx.fxml
  ```

  Example (macOS / Linux):

  ```text
  --module-path "/path/to/jdk/lib:target/classes" --add-modules javafx.controls,javafx.fxml
  ```

- Alternatively: use `./mvnw javafx:run` which configures JavaFX automatically via the Maven plugin.

Run (command line)

- Use the included Maven so you don't need Maven installed globally.

PowerShell example (temporary):

```powershell
$env:JAVA_HOME='C:\path\to\jdk'
$env:PATH="$env:JAVA_HOME\bin;$env:PATH"
.\mvnw clean test
.\mvnw clean javafx:run
```

Git Bash example (temporary):

```bash
export JAVA_HOME="/c/path/to/jdk"
export PATH="$JAVA_HOME/bin:$PATH"
./mvnw clean test
./mvnw clean javafx:run
```

Testing

- Unit tests are in `src/test/java/edu/utsa/cs3443/chrono/models` (JUnit 5).
- Run tests with `./mvnw test` or with IntelliJ's test runner.
- `TaskManagerTest` attempts to back up an existing `data/tasks.csv` and restore it after testing; it's intended to avoid permanent data loss, but you may prefer to refactor `TaskManager` for zero side effects in tests.

Files of interest

- `data/` - CSV files used at runtime (tasks, themes, cosmetics, etc.).
- `src/main/resources/edu/utsa/cs3443/chrono/layouts` - FXML layouts.
- `src/main/resources/edu/utsa/cs3443/chrono/css` - theme CSS files used by the Chrono.
- `src/main/resources/edu/utsa/cs3443/chrono/files` - the application runtime files (dailyTasks.csv, dailyProgress.txt, unlocks.txt).
- `src/main/java/edu/utsa/cs3443/chrono/models` - primary model classes (Task, TaskManager, TimerModel, etc.).

JavaFX/Scene Builder/Gluon (tools)

- Scene Builder (Our primary tool for UI Building): install Gluon's Scene Builder if you want a GUI tool to edit FXML files visually. Download: https://gluonhq.com/products/scene-builder/
- JavaFX runtime/SDK: you can either use the JavaFX libraries from Maven (the `org.openjfx` dependencies in `pom.xml`) or download a JavaFX SDK from Gluon/OpenJFX and point IntelliJ VM options at the SDK `lib` folder (see the IntelliJ VM options section).
- Gluon: Gluon provides the prebuilt JavaFX SDKs and SceneBuilder. If you prefer native SDKs (not Maven-managed), download the matching JavaFX SDK and set the `--module-path` VM option to its `lib` folder.
- IDE integration: IntelliJ can open FXML with SceneBuilder: in Settings > Languages & Frameworks > JavaFX point to the Scene Builder executable so `Open in SceneBuilder` works from the editor.

Tips

- If you run into missing JavaFX classes at runtime, prefer `./mvnw javafx:run` (it should configure the runtime automatically) or make sure to add the correct `--module-path` VM option in your Run Configuration.
- If you plan to edit FXML visually, install SceneBuilder and link it in IntelliJ. That prevents having to edit in native FXML (it can get convoluted).

Known issues

- Java version mismatches: `main` is configured for Java 24 so running with older JDKs (for example Java 21) will fail to compile.

  - Potential Mitigation: set your IntelliJ Project SDK to a matching JDK or install JDK 24

- JavaFX runtime/VM options may be required when running from the IDE: IntelliJ sometimes needs `--module-path` and `--add-modules` VM options to locate JavaFX modules.

  - Potential mitigation: either run `./mvnw javafx:run` which configures JavaFX automatically, or add the `--module-path`/`--add-modules` VM options to your Run Configuration (you can see the IntelliJ section above).

- The tests touch `data/tasks.csv`: `TaskManagerTest` backs up an existing `data/tasks.csv` and attempts to restore it after the test, but edge cases could cause temporary data changes.
  - Potential Mitigation: run tests on a copy of the project data, or request a refactor so tests use a temporary file path (zero side effects).

- No input validation for numeric fields: some UI controllers accept numeric input without strict validation; invalid input can cause unexpected behaviour.
  - Potential Mitigation: avoid entering invalid values for now
