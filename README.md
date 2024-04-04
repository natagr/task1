# Course Management System Overview

This document provides an overview of the Course Management System, detailing its main components, their purposes, key methods, and the relationship between courses and instructors. It also includes descriptions of input and output file formats and summarizes experimental results related to performance.

## Components

### UserInterface

- **Purpose**: Interacts with the user to select a specific attribute for statistical analysis.
- **Key Methods**:
  - `getAttributeFromUser()`: Prompts the user to select an attribute and returns the selected attribute's name.

### StatisticsCalculator

- **Purpose**: Provides utilities for computing and updating statistics based on arrays of string values, ensuring thread-safe operations suitable for concurrent environments.
- **Key Methods**:
  - `calculateStatistics(String[] values, Map<String, AtomicInteger> statistics)`: Analyzes an array of string values and counts occurrences of each unique value, updating the provided statistics map with these counts.

### FileParser

- **Purpose**: Parses JSON files in a directory asynchronously to gather statistics on specified attributes using multithreading.
- **Key Methods**:
  - `parseDirectory`: Scans a directory for JSON files and compiles attribute statistics.
  - `processFile & parseJson`: Extract and count attribute values from each file.
  - `shutdownAndAwaitTermination`: Ensures all threads are properly terminated.
  - `printStatistics`: Displays the count of processed and ignored records.

### XMLGenerator

- **Purpose**: Generates an XML file summarizing the collected statistics.
- **Key Methods**:
  - `generateXML(Map<String, Integer> statistics, String attribute)`: Generates an XML file detailing the frequency of each attribute value.

### PerformanceMeasurer

- **Purpose**: Measures and compares the performance of the FileParser operation with different numbers of threads.
- **Key Methods**:
  - `measurePerformance(String folderPath, String attribute, int[] threads)`: Measures execution time with varying thread counts and prints metrics.

### App

- **Purpose**: Entry point of the application, orchestrating operations from user input through performance measurement to XML report generation.
- **Key Actions**:
  - Prompts for attribute selection.
  - Benchmarks parsing performance.
  - Parses files, converts statistics for reporting.
  - Generates XML report.

## Input and Output Examples

### Input file

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
]```

Output file:
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

Results of experiments with the number of threads:

4 files:

Threads: 1 Time: 65 ms
Threads: 2 Time: 3 ms
Threads: 4 Time: 2 ms
Threads: 8 Time: 2 ms

Threads: 1 Time: 62 ms
Threads: 2 Time: 2 ms
Threads: 4 Time: 1 ms
Threads: 8 Time: 1 ms

Threads: 1 Time: 64 ms
Threads: 2 Time: 3 ms
Threads: 4 Time: 1 ms
Threads: 8 Time: 2 ms


16 files:

Threads: 1 Time: 129 ms
Threads: 2 Time: 6 ms
Threads: 4 Time: 3 ms
Threads: 8 Time: 4 ms

Threads: 1 Time: 89 ms
Threads: 2 Time: 5 ms
Threads: 4 Time: 4 ms
Threads: 8 Time: 4 ms

Threads: 1 Time: 68 ms
Threads: 2 Time: 5 ms
Threads: 4 Time: 3 ms
Threads: 8 Time: 4 ms


Summary:
The experimental results show that the optimal number of threads for file processing in this project corresponds to the number of processor cores, which in this case is 4. 
The results of experiments with processing 4 files show a significant improvement in execution time with an increase in the number of threads to 4. However, when moving from 4 to 8 threads, no further significant time reduction is observed, due to limitations on the number of processor cores and the fact that half of the threads do not contribute significantly to the overall processing performance, but at the same time create an additional load on the system due to the overhead of their creation and management.
When analyzing the results of processing 16 files, we observe a similar trend: increasing the number of threads to 4 leads to a significant reduction in processing time. This confirms the effectiveness of using the number of threads that corresponds to the number of processor cores on the device. However, when we further increase the number of threads to 8, the processing time either remains stable or even increases, which may indicate additional overhead for managing more threads than the number of processor cores.
