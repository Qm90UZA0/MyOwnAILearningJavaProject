/*

=================================================
本项目完全由DeepSeek生成, 因为这个程序我一点也不会写(
=================================================

*/
package tools;

import java.io.File;

public class SimpleTreeGenerator {
    
    public static void main(String[] args) {
        String projectPath = System.getProperty("user.dir");
        System.out.println("src目录结构:\n");
        
        File srcDir = new File(projectPath, "src");
        if (srcDir.exists()) {
            // 直接显示src/，不加前缀
            System.out.println("src/");
            printTreeContents(srcDir, "");
        } else {
            System.out.println("src目录不存在");
        }
    }
    
    private static void printTreeContents(File dir, String indent) {
        File[] children = dir.listFiles();
        if (children != null) {
            // 排序：目录在前，文件在后
            java.util.Arrays.sort(children, (f1, f2) -> {
                if (f1.isDirectory() && !f2.isDirectory()) return -1;
                if (!f1.isDirectory() && f2.isDirectory()) return 1;
                return f1.getName().compareToIgnoreCase(f2.getName());
            });
            
            for (int i = 0; i < children.length; i++) {
                File child = children[i];
                boolean isLast = (i == children.length - 1);
                
                System.out.print(indent + (isLast ? "└── " : "├── ") + child.getName());
                
                if (child.isDirectory()) {
                    System.out.println("/");
                    printTreeContents(child, indent + (isLast ? "    " : "│   "));
                } else {
                    System.out.println();
                }
            }
        }
    }
}