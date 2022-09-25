# princeton-algorithms

My solutions to the Princeton Algorithms Courses: [Algorithms, Part I](https://www.coursera.org/learn/algorithms-part1) and [Algorithms, Part II](https://www.coursera.org/learn/algorithms-part2) programming assignments on Coursera.

## Quick Start

Each project directory comes with the following items:

- Java source code: these are the code submitted to the AutoGrader
- `Makefile`: which provides 2 routines
  - `make submit`: which will compress the Java source code into a `.zip` in `out/` for submitting to the AutoGrader
  - `make clean`: remove directory `out/`
- `logo.png`: a nicely looking logo for each project
- `.gitignore`: which ignores all other files that came with the project boilerplate

Therefore, project-provided resources such as `algs4.jar`, testing clients, testing data and IDEA workspace settings are not managed by the repo. Please refer to the Project Specifications and Environment Setup for the latest resources of this such.

## Projects

|                   Project                   |           Code            |                                             Specification                                             | Grade |
|:-------------------------------------------:|:-------------------------:|:-----------------------------------------------------------------------------------------------------:|:-----:|
|   ![Percolation](./percolation/logo.png)    | [Code 💻](./percolation/) | [Specification 📖](https://coursera.cs.princeton.edu/algs4/assignments/percolation/specification.php) |  100  |
| ![Deques and Randomised](./queues/logo.png) |   [Code 💻](./queues/)    |   [Specification 📖](https://coursera.cs.princeton.edu/algs4/assignments/queues/specification.php)    |  100  |
|           ***Collinear Points***            |  [Code 💻](./collinear/)  |  [Specification 📖](https://coursera.cs.princeton.edu/algs4/assignments/collinear/specification.php)  |  100  |
|       ![8 Puzzle](./8puzzle/logo.png)       |   [Code 💻](./8puzzle/)   |   [Specification 📖](https://coursera.cs.princeton.edu/algs4/assignments/8puzzle/specification.php)   |  100  |
|       ![Kd-Trees](./kdtree/logo.png)        |   [Code 💻](./kdtree/)    |   [Specification 📖](https://coursera.cs.princeton.edu/algs4/assignments/kdtree/specification.php)    |  100  |
|       ![WordNet](./wordnet/logo.png)        |   [Code 💻](./wordnet/)   |   [Specification 📖](https://coursera.cs.princeton.edu/algs4/assignments/wordnet/specification.php)   |  100  |
|      ![Seam Carving](./seam/logo.png)       |    [Code 💻](./seam/)     |    [Specification 📖](https://coursera.cs.princeton.edu/algs4/assignments/seam/specification.php)     |  100  |

## Useful Resources

Here are some relevant resources that one might find useful.

- Environment Setup ([Mac](https://lift.cs.princeton.edu/java/mac/index.php), [Windows](https://lift.cs.princeton.edu/java/windows/), [Linux](https://lift.cs.princeton.edu/java/linux/)): a tutorial on how to set up the development environment locally.
- [Booksite](https://algs4.cs.princeton.edu/): the booksite for *Algorithms, 4th Edition* (Sedgewick and Wayne, 2011).
- [Code Repositories](https://algs4.cs.princeton.edu/code/): the Java source codes in the lectures and in the book.
- [`edu.princeton.cs.algs4` Javadoc](https://algs4.cs.princeton.edu/code/javadoc/): the Javadoc for the `edu.princeton.cs.algs4` package.
