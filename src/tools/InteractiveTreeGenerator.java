/*

=================================================
本项目完全由DeepSeek生成, 因为这个程序我一点也不会写(
=================================================

*/
package tools;

import java.io.File;
import java.util.Scanner;

public class InteractiveTreeGenerator {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String projectPath = System.getProperty("user.dir");
        
        System.out.println("当前项目路径: " + projectPath);
        System.out.println("\n请选择要生成的结构:");
        System.out.println("1. 整个项目结构");
        System.out.println("2. 仅src目录结构");
        System.out.println("3. 指定子目录结构");
        System.out.print("请选择 (1-3): ");
        
        int choice = scanner.nextInt();
        scanner.nextLine(); // 消耗换行符
        
        switch (choice) {
            case 1:
                System.out.println("\n整个项目结构:\n");
                generateTree(projectPath, true);
                break;
            case 2:
                System.out.println("\nsrc目录结构:\n");
                generateTree(projectPath + "/src", false);
                break;
            case 3:
                System.out.print("请输入子目录路径 (相对于项目根目录): ");
                String subPath = scanner.nextLine();
                generateTree(projectPath + "/" + subPath, false);
                break;
            default:
                System.out.println("无效选择");
        }
        
        scanner.close();
    }
    
    private static void generateTree(String path, boolean skipEclipseFiles) {
        File root = new File(path);
        if (!root.exists()) {
            System.out.println("路径不存在: " + path);
            return;
        }
        
        if (skipEclipseFiles) {
            printTreeWithSkip(root, "", true);
        } else {
            printTree(root, "", true);
        }
    }
    
    private static void printTreeWithSkip(File file, String indent, boolean isLast) {
        String name = file.getName();
        
        if (shouldSkip(name)) {
            return;
        }
        
        System.out.print(indent + (isLast ? "└── " : "├── ") + name);
        
        if (file.isDirectory()) {
            System.out.println("/");
            File[] children = file.listFiles();
            if (children != null) {
                java.util.Arrays.sort(children, (f1, f2) -> {
                    if (f1.isDirectory() && !f2.isDirectory()) return -1;
                    if (!f1.isDirectory() && f2.isDirectory()) return 1;
                    return f1.getName().compareToIgnoreCase(f2.getName());
                });
                
                for (int i = 0; i < children.length; i++) {
                    if (!shouldSkip(children[i].getName())) {
                        printTreeWithSkip(children[i], indent + (isLast ? "    " : "│   "), i == children.length - 1);
                    }
                }
            }
        } else {
            System.out.println();
        }
    }
    
    private static void printTree(File file, String indent, boolean isLast) {
        String name = file.getName();
        
        System.out.print(indent + (isLast ? "└── " : "├── ") + name);
        
        if (file.isDirectory()) {
            System.out.println("/");
            File[] children = file.listFiles();
            if (children != null) {
                java.util.Arrays.sort(children, (f1, f2) -> {
                    if (f1.isDirectory() && !f2.isDirectory()) return -1;
                    if (!f1.isDirectory() && f2.isDirectory()) return 1;
                    return f1.getName().compareToIgnoreCase(f2.getName());
                });
                
                for (int i = 0; i < children.length; i++) {
                    printTree(children[i], indent + (isLast ? "    " : "│   "), i == children.length - 1);
                }
            }
        } else {
            System.out.println();
        }
    }
    
    private static boolean shouldSkip(String name) {
        return name.equals("bin") || 
               name.equals("target") || 
               name.equals("build") ||
               name.equals(".settings") ||
               name.equals(".classpath") ||
               name.equals(".project") ||
               name.startsWith(".");
    }
}