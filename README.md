## Project Launch:

This project can be run through the Integrated Development Environment (IDE) IntelliJ IDEA (`Run -> Edit Configurations -> Application -> App -> Program arguments`).

### Required Launch Parameters

When launching the program through IntelliJ IDEA, the following launch parameters must be specified:

- **pathToFiles**: The absolute path to the folder containing JSON files.
- **attributeName**: The name of the attribute in the JSON file by which the statistics will be formed.

The project already includes a folder with sample JSON files. The path to this folder is: `src/main/resources/json`.

### Possible attributes for generating statistics include:

- `department`
- `credits`
- `instructor`

### Example of correctly entered parameters for launch:

src/main/resources/json department


## Subject area:

**Course - Instructor** (main - Course, secondary - Instructor)

Each course has one instructor, but one instructor can teach several different courses.

## Description of the main entities:

**Course:** 

- **courseName**:  Name of the course.
- **courseCode**: Unique code identifying the course.
- **courseDescription**: Brief overview of what the course entails.
- **credits**: Number of credit points the course carries.
- **department**: Departments under which the course is offered.
- **instructor**: Name of the instructor teaching the course.

**Instructor:** 

- **instructorID**: Unique identifier for the instructor.
- **name**: Full name of the instructor.
- **email**: Email address of the instructor.
- **officeLocation**: Physical location of the instructor's office.
- **officeHours**: Scheduled times when the instructor is available to meet with students.
- **departments**: Departments to which the instructor is affiliated.


## Components:

### FileParser

- **Purpose**: Parses JSON files in a directory asynchronously to gather statistics on specified attributes using multithreading.
- **Key Methods**:
  - `parseDirectory`: Scans a directory for JSON files and compiles attribute statistics.
  - `processFile & parseJson`: Extract and count attribute values from each file.
  - `shutdownAndAwaitTermination`: Ensures all threads are properly terminated.
  - `printStatistics`: Displays the count of processed and ignored records.
  - `convertToRegularMap`: Converts a map with AtomicInteger values to a map with Integer values.

### StatisticsCalculator

- **Purpose**: Provides utilities for computing and updating statistics based on arrays of string values, ensuring thread-safe operations suitable for concurrent environments.
- **Key Methods**:
  - `calculateStatistics`: Analyzes an array of string values and counts occurrences of each unique value, updating the provided statistics map with these counts.

### XMLGenerator

- **Purpose**: Generates an XML file summarizing the collected statistics.
- **Key Methods**:
  - `generateXML`: Generates an XML file detailing the frequency of each attribute value.

### PerformanceMeasurer

- **Purpose**: Measures and compares the performance of the FileParser operation with different numbers of threads.
- **Key Methods**:
  - `measurePerformance`: Measures execution time with varying thread counts and prints metrics.

### ParameterValidator

- **Purpose**: Provides validation for application input parameters to ensure they meet specific criteria before the application processes them. 
- **Key Methods:**
  - `validateArgsLength`: Checks if the arguments array contains at least two elements to proceed with further processing.
  - `validateFolderPath`: Validates that the given folder path exists and is a directory.
  - `validateAttribute`: Checks if the provided attribute is among the allowed attributes (`department`, `credits`, `instructor`).
  - `validateParameters`: Combines validation of the arguments length, folder path, and attribute in one method to streamline the validation process before the application performs any operations.

### App

- **Purpose**: Serves as the entry point of the application, orchestrating the flow of operations from files parsing through performance measurement and XML report generation.
- **Key Actions**:
  - Receives command line arguments.
  - Benchmarks parsing performance.
  - Parses files.
  - Generates XML report.


## Input and Output Examples:

### Input file:

```
[
  {
    "courseName": "Introduction to Programming",
    "courseCode": "CS101",
    "courseDescription": "An introductory course to programming concepts.",
    "credits": 56,
    "department": "Department of Robotics, Department of Electronics, Department of Engineering",
    "instructor": "John Doe4"
  },
  {
    "courseName": "Introduction to Programming",
    "courseCode": "CS102",
    "courseDescription": "An introductory course to programming concepts.",
    "credits": 34,
    "department": "Department of Computer Science, Department of Electronics",
    "instructor": "John Doe5"
  },
  {
    "courseName": "Introduction to Programming",
    "courseCode": "CS103",
    "courseDescription": "An introductory course to programming concepts.",
    "credits": 3,
    "department": "Department of Computer Science, Department of Electronics",
    "instructor": "John Doe5"
  }
]
```

### Output file:

```
<statistics>
  <item>
    <value>Department of Electronics</value>
    <count>3</count>
  </item>
  <item>
    <value>Department of Computer Science</value>
    <count>2</count>
  </item>
  <item>
    <value>Department of Robotics</value>
    <count>1</count>
  </item>
  <item>
    <value>Department of Engineering</value>
    <count>1</count>
  </item>
</statistics>
```


## Results of experiments with the number of threads:

| Number of Files | Threads | Measurement 1 (time, ms) | Measurement 2 (time, ms) | Measurement 3 (time, ms) |
|-----------------|---------|--------------------------|--------------------------|--------------------------|
| 4               | 1       | 65                       | 62                       | 64                       |
| 4               | 2       | 3                        | 2                        | 3                        |
| 4               | 4       | 2                        | 1                        | 1                        |
| 4               | 8       | 2                        | 1                        | 2                        |
| 16              | 1       | 129                      | 89                       | 68                       |
| 16              | 2       | 7                        | 6                        | 5                        |
| 16              | 4       | 4                        | 3                        | 4                        |
| 16              | 8       | 5                        | 4                        | 4                        |


## Summary:
The experimental results show that the optimal number of threads for file processing in this project corresponds to the number of processor cores, which in this case is 4. 

The results of experiments with processing 4 files show a significant improvement in execution time with an increase in the number of threads to 4. However, when moving from 4 to 8 threads, no further significant time reduction is observed, due to limitations on the number of processor cores and the fact that half of the threads do not contribute significantly to the overall processing performance, but at the same time create an additional load on the system due to the overhead of their creation and management.

When analyzing the results of processing 16 files, we observe a similar trend: increasing the number of threads to 4 leads to a significant reduction in processing time. This confirms the effectiveness of using the number of threads that corresponds to the number of processor cores on the device. However, when we further increase the number of threads to 8, the processing time either remains stable or even increases, which may indicate additional overhead for managing more threads than the number of processor cores.
