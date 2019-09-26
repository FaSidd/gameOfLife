plugins {
  java
  jacoco
  application
}       


application {
  mainClassName = "gui.GameOfLifeGUI"
}

repositories {
	mavenCentral()
}

dependencies {
  testCompile("org.junit.jupiter:junit-jupiter-api:5.2.0")
	testRuntime("org.junit.jupiter:junit-jupiter-engine:5.2.0")
	testRuntime("org.junit.platform:junit-platform-console:1.2.0")
}
 
sourceSets {
  main {
    java.srcDirs("gameoflife/src")
  }
  test {
    java.srcDirs("gameoflife/test")
  }
}

tasks {
    val treatWarningsAsError =
            listOf("-Xlint:unchecked", "-Xlint:deprecation", "-Werror")

    getByName<JavaCompile>("compileJava") {
        options.compilerArgs = treatWarningsAsError
    }

    getByName<JavaCompile>("compileTestJava") {
        options.compilerArgs = treatWarningsAsError
    }

    getByName<JacocoReport>("jacocoTestReport") {
        afterEvaluate {
            setClassDirectories(files(classDirectories.files.map {
                fileTree(it) { exclude("**/gui/**") }
            }))
        }
    }
}
val test by tasks.getting(Test::class) {
	useJUnitPlatform {}
}
 
defaultTasks("clean", "test", "jacocoTestReport")
