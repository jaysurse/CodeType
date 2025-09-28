import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import java.util.Timer;

public class CodeTypingApp extends JFrame {
    private JTextPane targetTextPane;
    private JTextField inputField;
    private JLabel timeLabel;
    private JLabel accuracyLabel;
    private JLabel wpmLabel;
    private JComboBox<String> languageSelector;
    private JButton startButton;
    private JButton resetButton;
    private JButton newTextButton;
    private JProgressBar progressBar;
    private JLabel titleLabel;

    private String currentText = "";
    private int currentPosition = 0;
    private long startTime;
    private Timer timer;
    private boolean testStarted = false;
    private boolean testCompleted = false;

    private int correctChars = 0;
    private int totalChars = 0;
    private int errors = 0;

    // Code samples for different languages
    private Map<String, String[]> codeSamples;

    // Enhanced color scheme
    private static final Color PRIMARY_COLOR = new Color(79, 70, 229);
    private static final Color SECONDARY_COLOR = new Color(236, 72, 153);
    private static final Color SUCCESS_COLOR = new Color(34, 197, 94);
    private static final Color ERROR_COLOR = new Color(239, 68, 68);
    private static final Color WARNING_COLOR = new Color(245, 158, 11);
    private static final Color CURRENT_COLOR = new Color(59, 130, 246);
    private static final Color UNTYPED_COLOR = new Color(148, 163, 184);
    private static final Color BACKGROUND_COLOR = new Color(15, 23, 42);
    private static final Color PANEL_COLOR = new Color(30, 41, 59);
    private static final Color CARD_COLOR = new Color(51, 65, 85);
    private static final Color TEXT_PRIMARY = new Color(248, 250, 252);
    private static final Color TEXT_SECONDARY = new Color(203, 213, 225);

    public CodeTypingApp() {
        initializeCodeSamples();
        initializeGUI();
        setupEventListeners();
    }

    private void initializeCodeSamples() {
        codeSamples = new HashMap<>();

        codeSamples.put("Java", new String[] {
                "public class HelloWorld {\n    public static void main(String[] args) {\n        System.out.println(\"Hello, World!\");\n        int[] numbers = {1, 2, 3, 4, 5};\n        Arrays.stream(numbers).forEach(System.out::println);\n    }\n}",
                "public class BinarySearch {\n    public static int search(int[] arr, int target) {\n        int left = 0, right = arr.length - 1;\n        while (left <= right) {\n            int mid = left + (right - left) / 2;\n            if (arr[mid] == target) return mid;\n            if (arr[mid] < target) left = mid + 1;\n            else right = mid - 1;\n        }\n        return -1;\n    }\n}",
                "@Override\npublic boolean equals(Object obj) {\n    if (this == obj) return true;\n    if (obj == null || getClass() != obj.getClass()) return false;\n    Person person = (Person) obj;\n    return age == person.age && Objects.equals(name, person.name);\n}",
                "public class LinkedList<T> {\n    private Node<T> head;\n    \n    private static class Node<T> {\n        T data;\n        Node<T> next;\n        \n        Node(T data) {\n            this.data = data;\n        }\n    }\n    \n    public void add(T item) {\n        if (head == null) {\n            head = new Node<>(item);\n        } else {\n            Node<T> current = head;\n            while (current.next != null) {\n                current = current.next;\n            }\n            current.next = new Node<>(item);\n        }\n    }\n}"
        });

        codeSamples.put("Python", new String[] {
                "def fibonacci_generator(n):\n    \"\"\"Generate fibonacci sequence up to n terms\"\"\"\n    a, b = 0, 1\n    for _ in range(n):\n        yield a\n        a, b = b, a + b\n\n# Usage\nfib_nums = list(fibonacci_generator(10))\nprint(f\"First 10 fibonacci numbers: {fib_nums}\")",
                "class DataProcessor:\n    def __init__(self, data):\n        self.data = data\n        self.processed = False\n    \n    def process(self):\n        \"\"\"Process the data with error handling\"\"\"\n        try:\n            result = [x * 2 for x in self.data if isinstance(x, (int, float))]\n            self.processed = True\n            return result\n        except Exception as e:\n            print(f\"Processing error: {e}\")\n            return None",
                "import asyncio\nimport aiohttp\n\nasync def fetch_data(session, url):\n    \"\"\"Asynchronously fetch data from URL\"\"\"\n    try:\n        async with session.get(url) as response:\n            return await response.json()\n    except aiohttp.ClientError as e:\n        print(f\"Request failed: {e}\")\n        return None\n\nasync def main():\n    urls = ['https://api1.com', 'https://api2.com']\n    async with aiohttp.ClientSession() as session:\n        tasks = [fetch_data(session, url) for url in urls]\n        results = await asyncio.gather(*tasks)\n    return results",
                "from dataclasses import dataclass\nfrom typing import List, Optional\n\n@dataclass\nclass Student:\n    name: str\n    age: int\n    grades: List[float]\n    email: Optional[str] = None\n    \n    def average_grade(self) -> float:\n        return sum(self.grades) / len(self.grades) if self.grades else 0.0\n    \n    def is_passing(self, threshold: float = 60.0) -> bool:\n        return self.average_grade() >= threshold"
        });

        codeSamples.put("JavaScript", new String[] {
                "const debounce = (func, delay) => {\n    let timeoutId;\n    return (...args) => {\n        clearTimeout(timeoutId);\n        timeoutId = setTimeout(() => func.apply(null, args), delay);\n    };\n};\n\n// Usage\nconst debouncedSearch = debounce((query) => {\n    console.log(`Searching for: ${query}`);\n    // Perform search operation\n}, 300);",
                "class EventEmitter {\n    constructor() {\n        this.events = new Map();\n    }\n    \n    on(event, callback) {\n        if (!this.events.has(event)) {\n            this.events.set(event, []);\n        }\n        this.events.get(event).push(callback);\n        return this;\n    }\n    \n    emit(event, ...args) {\n        const callbacks = this.events.get(event);\n        if (callbacks) {\n            callbacks.forEach(callback => callback(...args));\n        }\n        return this;\n    }\n}",
                "const fetchWithRetry = async (url, options = {}, maxRetries = 3) => {\n    for (let attempt = 1; attempt <= maxRetries; attempt++) {\n        try {\n            const response = await fetch(url, options);\n            if (!response.ok) {\n                throw new Error(`HTTP ${response.status}: ${response.statusText}`);\n            }\n            return await response.json();\n        } catch (error) {\n            console.warn(`Attempt ${attempt} failed:`, error.message);\n            if (attempt === maxRetries) throw error;\n            await new Promise(resolve => setTimeout(resolve, 1000 * attempt));\n        }\n    }\n};",
                "const createStore = (reducer, initialState) => {\n    let state = initialState;\n    const listeners = new Set();\n    \n    return {\n        getState: () => state,\n        dispatch: (action) => {\n            state = reducer(state, action);\n            listeners.forEach(listener => listener(state));\n        },\n        subscribe: (listener) => {\n            listeners.add(listener);\n            return () => listeners.delete(listener);\n        }\n    };\n};"
        });

        codeSamples.put("TypeScript", new String[] {
                "interface User {\n    id: number;\n    name: string;\n    email: string;\n    isActive: boolean;\n}\n\ntype CreateUserRequest = Omit<User, 'id'>;\n\nclass UserService {\n    private users: User[] = [];\n    \n    async createUser(userData: CreateUserRequest): Promise<User> {\n        const newUser: User = {\n            id: Date.now(),\n            ...userData\n        };\n        this.users.push(newUser);\n        return newUser;\n    }\n}",
                "function createApiClient<T extends Record<string, unknown>>(\n    baseUrl: string,\n    defaultHeaders: HeadersInit = {}\n) {\n    return {\n        async get<R = T>(endpoint: string): Promise<R> {\n            const response = await fetch(`${baseUrl}${endpoint}`, {\n                headers: defaultHeaders\n            });\n            return response.json();\n        },\n        \n        async post<R = T>(endpoint: string, data: Partial<T>): Promise<R> {\n            const response = await fetch(`${baseUrl}${endpoint}`, {\n                method: 'POST',\n                headers: { 'Content-Type': 'application/json', ...defaultHeaders },\n                body: JSON.stringify(data)\n            });\n            return response.json();\n        }\n    };\n}",
                "enum Status {\n    PENDING = 'pending',\n    APPROVED = 'approved',\n    REJECTED = 'rejected'\n}\n\ninterface Task<T = any> {\n    id: string;\n    title: string;\n    status: Status;\n    data?: T;\n    createdAt: Date;\n}\n\nclass TaskManager<T> {\n    private tasks: Map<string, Task<T>> = new Map();\n    \n    addTask(task: Omit<Task<T>, 'id' | 'createdAt'>): Task<T> {\n        const newTask: Task<T> = {\n            id: crypto.randomUUID(),\n            createdAt: new Date(),\n            ...task\n        };\n        this.tasks.set(newTask.id, newTask);\n        return newTask;\n    }\n}"
        });

        codeSamples.put("C++", new String[] {
                "#include <iostream>\n#include <vector>\n#include <algorithm>\n#include <memory>\n\ntemplate<typename T>\nclass SmartVector {\nprivate:\n    std::unique_ptr<T[]> data;\n    size_t size_;\n    size_t capacity_;\n    \npublic:\n    SmartVector(size_t capacity = 10) \n        : data(std::make_unique<T[]>(capacity)), size_(0), capacity_(capacity) {}\n    \n    void push_back(const T& value) {\n        if (size_ >= capacity_) {\n            resize();\n        }\n        data[size_++] = value;\n    }\n    \n    T& operator[](size_t index) {\n        if (index >= size_) throw std::out_of_range(\"Index out of bounds\");\n        return data[index];\n    }\n};",
                "class ResourceManager {\nprivate:\n    std::map<std::string, std::shared_ptr<Resource>> resources;\n    mutable std::mutex resourceMutex;\n    \npublic:\n    template<typename T, typename... Args>\n    std::shared_ptr<T> createResource(const std::string& name, Args&&... args) {\n        std::lock_guard<std::mutex> lock(resourceMutex);\n        auto resource = std::make_shared<T>(std::forward<Args>(args)...);\n        resources[name] = resource;\n        return resource;\n    }\n    \n    std::shared_ptr<Resource> getResource(const std::string& name) const {\n        std::lock_guard<std::mutex> lock(resourceMutex);\n        auto it = resources.find(name);\n        return (it != resources.end()) ? it->second : nullptr;\n    }\n};",
                "namespace Algorithms {\n    template<typename Iterator, typename Predicate>\n    Iterator partition(Iterator first, Iterator last, Predicate pred) {\n        while (true) {\n            while (first != last && pred(*first)) {\n                ++first;\n            }\n            if (first == last) break;\n            \n            do {\n                --last;\n            } while (first != last && !pred(*last));\n            \n            if (first == last) break;\n            \n            std::iter_swap(first, last);\n            ++first;\n        }\n        return first;\n    }\n}",
                "class ThreadPool {\nprivate:\n    std::vector<std::thread> workers;\n    std::queue<std::function<void()>> tasks;\n    std::mutex queueMutex;\n    std::condition_variable condition;\n    bool stop;\n    \npublic:\n    ThreadPool(size_t numThreads) : stop(false) {\n        for (size_t i = 0; i < numThreads; ++i) {\n            workers.emplace_back([this] {\n                while (true) {\n                    std::function<void()> task;\n                    {\n                        std::unique_lock<std::mutex> lock(queueMutex);\n                        condition.wait(lock, [this] { return stop || !tasks.empty(); });\n                        if (stop && tasks.empty()) return;\n                        task = std::move(tasks.front());\n                        tasks.pop();\n                    }\n                    task();\n                }\n            });\n        }\n    }\n}"
        });
    }

    private void initializeGUI() {
        setTitle("CodeType Pro - Advanced Programming Typing Test");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setResizable(true);

        // Set the modern look and feel
        // UIManager look and feel removed for compatibility

        // Create main content panel with gradient background
        JPanel mainPanel = new GradientPanel();
        mainPanel.setLayout(new BorderLayout(0, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Header with title and controls
        JPanel headerPanel = createHeaderPanel();

        // Stats panel with cards
        JPanel statsPanel = createStatsPanel();

        // Code display area
        JPanel codePanel = createCodePanel();

        // Input area
        JPanel inputPanel = createInputPanel();

        // Assembly
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 20));
        centerPanel.setOpaque(false);
        centerPanel.add(statsPanel, BorderLayout.NORTH);
        centerPanel.add(codePanel, BorderLayout.CENTER);
        centerPanel.add(inputPanel, BorderLayout.SOUTH);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        add(mainPanel);

        loadNewText();
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        // Title
        titleLabel = new JLabel("CodeType Pro", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(TEXT_PRIMARY);

        // Subtitle
        JLabel subtitleLabel = new JLabel("Master Your Programming Typing Skills", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(TEXT_SECONDARY);

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        titlePanel.add(subtitleLabel, BorderLayout.SOUTH);

        // Controls
        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        controlsPanel.setOpaque(false);

        JLabel languageLabel = new JLabel("Language:");
        languageLabel.setForeground(TEXT_PRIMARY);
        languageLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        languageSelector = new JComboBox<>(new String[] { "Java", "Python", "JavaScript", "TypeScript", "C++" });
        languageSelector.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        styleComboBox(languageSelector);

        startButton = createStyledButton("Start Test", PRIMARY_COLOR, SUCCESS_COLOR);
        resetButton = createStyledButton("Reset", new Color(107, 114, 128), WARNING_COLOR);
        newTextButton = createStyledButton("New Code", SECONDARY_COLOR, PRIMARY_COLOR);

        controlsPanel.add(languageLabel);
        controlsPanel.add(languageSelector);
        controlsPanel.add(startButton);
        controlsPanel.add(resetButton);
        controlsPanel.add(newTextButton);

        headerPanel.add(titlePanel, BorderLayout.CENTER);
        headerPanel.add(controlsPanel, BorderLayout.SOUTH);

        return headerPanel;
    }

    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 20, 0));
        panel.setOpaque(false);

        // Time card
        timeLabel = createStatCard("Time", "00:00", WARNING_COLOR);

        // WPM card
        wpmLabel = createStatCard("WPM", "0", SUCCESS_COLOR);

        // Accuracy card
        accuracyLabel = createStatCard("Accuracy", "100%", PRIMARY_COLOR);

        // Progress card
        JPanel progressCard = createCardPanel();
        JLabel progressTitle = new JLabel("Progress", SwingConstants.CENTER);
        progressTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        progressTitle.setForeground(TEXT_SECONDARY);

        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setString("0%");
        progressBar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        styleProgressBar(progressBar);

        progressCard.add(progressTitle, BorderLayout.NORTH);
        progressCard.add(progressBar, BorderLayout.CENTER);

        panel.add(timeLabel);
        panel.add(wpmLabel);
        panel.add(accuracyLabel);
        panel.add(progressCard);

        return panel;
    }

    private JLabel createStatCard(String title, String value, Color accentColor) {
        JPanel card = createCardPanel();

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(TEXT_SECONDARY);

        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valueLabel.setForeground(accentColor);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        // Create a wrapper label to return
        JLabel wrapper = new JLabel();
        wrapper.setLayout(new BorderLayout());
        wrapper.add(card, BorderLayout.CENTER);

        return wrapper;
    }

    private JPanel createCardPanel() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(15, CARD_COLOR),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        return card;
    }

    private JPanel createCodePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        targetTextPane = new JTextPane();
        targetTextPane.setEditable(false);
        targetTextPane.setFont(new Font("JetBrains Mono", Font.PLAIN, 18));
        targetTextPane.setBackground(CARD_COLOR);
        targetTextPane.setForeground(UNTYPED_COLOR);
        targetTextPane.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        targetTextPane.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(targetTextPane);
        scrollPane.setPreferredSize(new Dimension(0, 350));
        scrollPane.setBorder(new RoundedBorder(15, CARD_COLOR));
        scrollPane.getViewport().setBackground(CARD_COLOR);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Style scrollbars
        styleScrollBar(scrollPane.getVerticalScrollBar());

        JLabel codeTitle = new JLabel("Code to Type", SwingConstants.CENTER);
        codeTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        codeTitle.setForeground(TEXT_PRIMARY);
        codeTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        panel.add(codeTitle, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setOpaque(false);

        JLabel inputTitle = new JLabel("Your Input", SwingConstants.CENTER);
        inputTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        inputTitle.setForeground(TEXT_PRIMARY);

        inputField = new JTextField();
        inputField.setFont(new Font("JetBrains Mono", Font.PLAIN, 18));
        inputField.setBackground(CARD_COLOR);
        inputField.setForeground(TEXT_PRIMARY);
        inputField.setCaretColor(PRIMARY_COLOR);
        inputField.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(12, CARD_COLOR),
                BorderFactory.createEmptyBorder(20, 25, 20, 25)));
        inputField.setPreferredSize(new Dimension(0, 75));

        // Add focus effects
        inputField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                inputField.setBorder(BorderFactory.createCompoundBorder(
                        new RoundedBorder(12, PRIMARY_COLOR, 2),
                        BorderFactory.createEmptyBorder(20, 25, 20, 25)));
            }

            @Override
            public void focusLost(FocusEvent e) {
                inputField.setBorder(BorderFactory.createCompoundBorder(
                        new RoundedBorder(12, CARD_COLOR),
                        BorderFactory.createEmptyBorder(20, 25, 20, 25)));
            }
        });

        panel.add(inputTitle, BorderLayout.NORTH);
        panel.add(inputField, BorderLayout.CENTER);

        return panel;
    }

    private JButton createStyledButton(String text, Color bgColor, Color hoverColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color color = getModel().isRollover() ? hoverColor : bgColor;
                g2.setColor(color);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                g2.setColor(Color.WHITE);
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent()) / 2 - 2;
                g2.drawString(getText(), x, y);

                g2.dispose();
            }
        };

        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(120, 40));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        return button;
    }

    private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setBackground(CARD_COLOR);
        comboBox.setForeground(TEXT_PRIMARY);
        comboBox.setBorder(new RoundedBorder(8, CARD_COLOR));
        comboBox.setPreferredSize(new Dimension(150, 35));
        comboBox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private void styleProgressBar(JProgressBar progressBar) {
        progressBar.setBackground(PANEL_COLOR);
        progressBar.setForeground(SUCCESS_COLOR);
        progressBar.setBorder(new RoundedBorder(8, PANEL_COLOR));
        progressBar.setPreferredSize(new Dimension(0, 25));
    }

    private void styleScrollBar(JScrollBar scrollBar) {
        scrollBar.setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = PANEL_COLOR;
                this.trackColor = CARD_COLOR;
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                button.setMinimumSize(new Dimension(0, 0));
                button.setMaximumSize(new Dimension(0, 0));
                return button;
            }

            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(thumbColor);
                g2.fillRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height, 10, 10);
                g2.dispose();
            }
        });
    }

    private void setupEventListeners() {
        startButton.addActionListener(e -> startTest());
        resetButton.addActionListener(e -> resetTest());
        newTextButton.addActionListener(e -> loadNewText());
        languageSelector.addActionListener(e -> loadNewText());

        inputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!testStarted && e.getKeyCode() != KeyEvent.VK_TAB) {
                    startTest();
                }
            }
        });

        inputField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateText();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateText();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateText();
            }

            private void updateText() {
                if (testStarted && !testCompleted) {
                    SwingUtilities.invokeLater(() -> checkInput());
                }
            }
        });
    }

    private void loadNewText() {
        String selectedLanguage = (String) languageSelector.getSelectedItem();
        String[] samples = codeSamples.get(selectedLanguage);
        currentText = samples[new Random().nextInt(samples.length)];

        targetTextPane.setText(currentText);
        updateTextDisplay();
        resetTest();
    }

    private void startTest() {
        if (!testStarted) {
            testStarted = true;
            startTime = System.currentTimeMillis();
            inputField.setEnabled(true);
            inputField.requestFocus();
            startButton.setEnabled(false);

            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    SwingUtilities.invokeLater(() -> updateStats());
                }
            }, 0, 100);
        }
    }

    private void resetTest() {
        testStarted = false;
        testCompleted = false;
        currentPosition = 0;
        correctChars = 0;
        totalChars = 0;
        errors = 0;

        inputField.setText("");
        inputField.setEnabled(true);
        startButton.setEnabled(true);

        if (timer != null) {
            timer.cancel();
        }

        updateStatLabels("00:00", "0", "100%", 0);
        updateTextDisplay();
    }

    private void updateStatLabels(String time, String wpm, String accuracy, int progress) {
        // Update time card
        JPanel timeCard = (JPanel) ((JLabel) timeLabel).getComponent(0);
        JLabel timeValue = (JLabel) timeCard.getComponent(1);
        timeValue.setText(time);

        // Update WPM card
        JPanel wpmCard = (JPanel) ((JLabel) wpmLabel).getComponent(0);
        JLabel wpmValue = (JLabel) wpmCard.getComponent(1);
        wpmValue.setText(wpm);

        // Update accuracy card
        JPanel accuracyCard = (JPanel) ((JLabel) accuracyLabel).getComponent(0);
        JLabel accuracyValue = (JLabel) accuracyCard.getComponent(1);
        accuracyValue.setText(accuracy);

        // Update progress bar
        progressBar.setValue(progress);
        progressBar.setString(progress + "%");
    }

    private void checkInput() {
        String input = inputField.getText();
        currentPosition = input.length();

        if (currentPosition > currentText.length()) {
            return;
        }

        // Check if test is completed
        if (input.equals(currentText)) {
            completeTest();
            return;
        }

        updateTextDisplay();

        // Update progress
        int progress = (int) ((double) currentPosition / currentText.length() * 100);
        progressBar.setValue(progress);
        progressBar.setString(progress + "%");
    }

    private void updateTextDisplay() {
        try {
            StyledDocument doc = targetTextPane.getStyledDocument();
            doc.remove(0, doc.getLength());

            String input = inputField.getText();
            int inputLength = Math.min(input.length(), currentText.length());

            // Add typed text with appropriate colors
            for (int i = 0; i < inputLength; i++) {
                SimpleAttributeSet attrs = new SimpleAttributeSet();
                if (input.charAt(i) == currentText.charAt(i)) {
                    StyleConstants.setForeground(attrs, SUCCESS_COLOR);
                    StyleConstants.setBold(attrs, true);
                } else {
                    StyleConstants.setForeground(attrs, ERROR_COLOR);
                    StyleConstants.setBackground(attrs, new Color(239, 68, 68, 30));
                    StyleConstants.setBold(attrs, true);
                }
                doc.insertString(doc.getLength(), String.valueOf(currentText.charAt(i)), attrs);
            }

            // Add current character (cursor position)
            if (inputLength < currentText.length()) {
                SimpleAttributeSet currentAttrs = new SimpleAttributeSet();
                StyleConstants.setBackground(currentAttrs, CURRENT_COLOR);
                StyleConstants.setForeground(currentAttrs, Color.WHITE);
                StyleConstants.setBold(currentAttrs, true);
                doc.insertString(doc.getLength(), String.valueOf(currentText.charAt(inputLength)), currentAttrs);

                // Add remaining text
                if (inputLength + 1 < currentText.length()) {
                    SimpleAttributeSet untypedAttrs = new SimpleAttributeSet();
                    StyleConstants.setForeground(untypedAttrs, UNTYPED_COLOR);
                    doc.insertString(doc.getLength(), currentText.substring(inputLength + 1), untypedAttrs);
                }
            }

            // Auto-scroll to keep current position visible
            targetTextPane.setCaretPosition(Math.min(inputLength, doc.getLength()));
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private void updateStats() {
        if (!testStarted || testCompleted)
            return;

        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - startTime;

        // Update time display
        int seconds = (int) (elapsedTime / 1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;
        String timeString = String.format("%02d:%02d", minutes, seconds);

        // Calculate WPM and accuracy
        String input = inputField.getText();
        int correctChars = 0;
        int totalChars = input.length();

        for (int i = 0; i < Math.min(input.length(), currentText.length()); i++) {
            if (input.charAt(i) == currentText.charAt(i)) {
                correctChars++;
            }
        }

        double timeInMinutes = elapsedTime / 60000.0;
        int wpm = timeInMinutes > 0 ? (int) ((correctChars / 5.0) / timeInMinutes) : 0;
        double accuracy = totalChars > 0 ? (correctChars * 100.0 / totalChars) : 100.0;
        int progress = currentText.length() > 0 ? (int) ((double) totalChars / currentText.length() * 100) : 0;

        updateStatLabels(timeString, String.valueOf(wpm), String.format("%.1f%%", accuracy), progress);
    }

    private void completeTest() {
        testCompleted = true;
        inputField.setEnabled(false);

        if (timer != null) {
            timer.cancel();
        }

        // Final statistics
        long totalTime = System.currentTimeMillis() - startTime;
        double timeInMinutes = totalTime / 60000.0;
        int finalWPM = (int) ((currentText.length() / 5.0) / timeInMinutes);

        // Create custom completion dialog
        showCompletionDialog(finalWPM, totalTime / 1000.0);

        startButton.setEnabled(true);
        progressBar.setValue(100);
        progressBar.setString("Complete!");
    }

    private void showCompletionDialog(int wpm, double timeSeconds) {
        JDialog dialog = new JDialog(this, "Test Completed!", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        // Create content panel with gradient background
        JPanel contentPanel = new GradientPanel();
        contentPanel.setLayout(new BorderLayout(20, 20));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Title
        JLabel titleLabel = new JLabel("🎉 Congratulations!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_PRIMARY);

        // Stats panel
        JPanel statsPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        statsPanel.setOpaque(false);

        JLabel wpmLabel = new JLabel(String.format("Final WPM: %d", wpm), SwingConstants.CENTER);
        wpmLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        wpmLabel.setForeground(SUCCESS_COLOR);

        JLabel timeLabel = new JLabel(String.format("Time: %.1f seconds", timeSeconds), SwingConstants.CENTER);
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        timeLabel.setForeground(TEXT_SECONDARY);

        JLabel accuracyLabel = new JLabel("Accuracy: 100%", SwingConstants.CENTER);
        accuracyLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        accuracyLabel.setForeground(TEXT_SECONDARY);

        statsPanel.add(wpmLabel);
        statsPanel.add(timeLabel);
        statsPanel.add(accuracyLabel);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setOpaque(false);

        JButton newTestButton = createStyledButton("New Test", SUCCESS_COLOR, PRIMARY_COLOR);
        JButton closeButton = createStyledButton("Close", new Color(107, 114, 128), ERROR_COLOR);

        newTestButton.addActionListener(e -> {
            dialog.dispose();
            loadNewText();
        });

        closeButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(newTestButton);
        buttonPanel.add(closeButton);

        contentPanel.add(titleLabel, BorderLayout.NORTH);
        contentPanel.add(statsPanel, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.add(contentPanel);
        dialog.setVisible(true);
    }

    // Custom gradient panel for background
    private static class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            GradientPaint gradient = new GradientPaint(
                    0, 0, BACKGROUND_COLOR,
                    0, getHeight(), PANEL_COLOR);
            g2.setPaint(gradient);
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
        }
    }

    // Custom rounded border class
    private static class RoundedBorder extends AbstractBorder {
        private final int radius;
        private final Color color;
        private final int thickness;

        public RoundedBorder(int radius, Color color) {
            this(radius, color, 1);
        }

        public RoundedBorder(int radius, Color color, int thickness) {
            this.radius = radius;
            this.color = color;
            this.thickness = thickness;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(thickness));
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(thickness, thickness, thickness, thickness);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = insets.top = insets.right = insets.bottom = thickness;
            return insets;
        }
    }

    public static void main(String[] args) {
        // Set system properties for better rendering
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        // UIManager look and feel removed for compatibility

        SwingUtilities.invokeLater(() -> {
            new CodeTypingApp().setVisible(true);
        });
    }
}