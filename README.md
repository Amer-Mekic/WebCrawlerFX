# WebCrawlerFX

A desktop GUI Web Crawler built with **Java 24** and **JavaFX 21**, showing crawled pages as a tree structure.

---

## Features

- Enter any starting URL
- Limit crawl by depth and links per page
- Visual tree display of links (using `TreeView`)
- JavaFX GUI with loading screen / progress bar

---

## Requirements

- Java **JDK 21+** (written with Java 24)
- JavaFX **SDK 21** or later

> !!! JavaFX is not bundled with JDK 11+ — download and install it manually. !!!

---

## JavaFX SDK Setup

1. Download from: https://gluonhq.com/products/javafx/
2. Extract and note the path to the `lib` folder (e.g. `C:/javafx-sdk-21/lib`)

---

## How to Run the App

Download the `.jar` file from [Releases](https://github.com/Amer-Mekic/WebCrawlerFX/releases/tag/v0.5), then open a terminal and run:

```bash
java --module-path "C:\path\to\javafx-sdk-21\lib" ^
     --add-modules javafx.controls,javafx.fxml ^
     -jar WebCrawler.jar
```
Replace the path with your actual JavaFX SDK `lib` path.

---

### On Linux/macOS

Use `\` for line continuation in the shell:

```bash
java --module-path "/path/to/javafx-sdk-21/lib" \
     --add-modules javafx.controls,javafx.fxml \
     -jar WebCrawler.jar
```

---

## Example Usage

Start with:

https://example.com

Watch as the tree view fills up with links!

---

## Notes

- JavaFX support requires a proper `--module-path`
- **Do not** double-click the `.jar` file unless JavaFX is on your system path
- Use the command line instructions provided above to run the application