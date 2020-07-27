package org.simpleyaml.configuration.file;

import java.io.File;
import java.net.URL;
import java.util.Objects;
import org.cactoos.io.TempFile;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNot;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.IsTrue;
import org.simpleyaml.configuration.comments.CommentType;

class YamlFileTest {

    private static String getResourcePath(final String file) {
        return Objects.requireNonNull(YamlFileTest.getResourceURL(file)).getPath();
    }

    private static URL getResourceURL(final String file) {
        return YamlFileTest.class.getClassLoader().getResource(file);
    }

    @Test
    void load() throws Exception {
        final YamlFile yamlFile = new YamlFile(YamlFileTest.getResourcePath("test.yml"));
        final String content = "test:\n" +
            "  number: 5\n" +
            "  string: Hello world\n" +
            "  boolean: true\n" +
            "  list:\n" +
            "  - Each\n" +
            "  - word\n" +
            "  - will\n" +
            "  - be\n" +
            "  - in\n" +
            "  - a\n" +
            "  - separated\n" +
            "  - entry\n" +
            "math:\n" +
            "  pi: 3.141592653589793\n" +
            "timestamp:\n" +
            "  canonicalDate: 2020-07-04T13:18:04.458Z\n" +
            "  formattedDate: 04/07/2020 15:18:04\n";
        yamlFile.load();
        MatcherAssert.assertThat(
            "Couldn't load the file!",
            yamlFile.saveToStringWithComments(),
            new IsEqual<>(content)
        );
        yamlFile.load(YamlFileTest.getResourcePath("test.yml"));
        MatcherAssert.assertThat(
            "Couldn't load the file!",
            yamlFile.saveToStringWithComments(),
            new IsEqual<>(content)
        );
        yamlFile.load(YamlFileTest.getResourceURL("test.yml").openStream());
        MatcherAssert.assertThat(
            "Couldn't load the file!",
            yamlFile.saveToStringWithComments(),
            new IsEqual<>(content)
        );
        yamlFile.load(new File(YamlFileTest.getResourceURL("test.yml").getFile()));
        MatcherAssert.assertThat(
            "Couldn't load the file!",
            yamlFile.saveToStringWithComments(),
            new IsEqual<>(content)
        );
    }

    @Test
    void loadWithComments() throws Exception {
        final YamlFile yamlFile = new YamlFile(YamlFileTest.getResourcePath("test-comments.yml"));
        final String content = "#####################\n" +
            "## INITIAL COMMENT ##\n" +
            "#####################\n" +
            '\n' +
            "# Test comments\n" +
            "test:\n" +
            "  number: 5\n" +
            "  # Hello!\n" +
            "  string: Hello world\n" +
            "  boolean: true\n" +
            "  # List of words\n" +
            "  list:\n" +
            "  - Each\n" +
            "  - word\n" +
            "  - will\n" +
            "  - be\n" +
            "  - in\n" +
            "  - a\n" +
            "  - separated\n" +
            "    # Comment on a list item\n" +
            "  - entry # :)\n" +
            '\n' +
            "# Wonderful number\n" +
            "math:\n" +
            "  pi: 3.141592653589793\n" +
            "  # Comment without direct key\n" +
            '\n' +
            "# Some timestamps\n" +
            "timestamp:\n" +
            "  # ISO\n" +
            "  canonicalDate: 2020-07-04T13:18:04.458Z\n" +
            "  # Date/Time with format\n" +
            "  formattedDate: 04/07/2020 15:18:04\n" +
            '\n' +
            "# End\n";
        yamlFile.loadWithComments();
        MatcherAssert.assertThat(
            "Couldn't load the file with comments!",
            yamlFile.saveToStringWithComments(),
            new IsEqual<>(content)
        );
    }

    @Test
    void createOrLoad() throws Exception {
        final YamlFile yamlFile = new YamlFile(YamlFileTest.getResourcePath("test-comments.yml"));
        final String content = "# ####################\n" +
            "# # INITIAL COMMENT ##\n" +
            "# ####################\n" +
            "# \n" +
            "# Test comments\n" +
            "test:\n" +
            "  number: 5\n" +
            "  string: Hello world\n" +
            "  boolean: true\n" +
            "  list:\n" +
            "  - Each\n" +
            "  - word\n" +
            "  - will\n" +
            "  - be\n" +
            "  - in\n" +
            "  - a\n" +
            "  - separated\n" +
            "  - entry\n" +
            "math:\n" +
            "  pi: 3.141592653589793\n" +
            "timestamp:\n" +
            "  canonicalDate: 2020-07-04T13:18:04.458Z\n" +
            "  formattedDate: 04/07/2020 15:18:04\n";
        yamlFile.createOrLoad();
        MatcherAssert.assertThat(
            "Couldn't load the file with comments!",
            yamlFile.saveToString(),
            new IsEqual<>(content)
        );
    }

    @Test
    void createOrLoadWithComments() throws Exception {
        final YamlFile yamlFile = new YamlFile(YamlFileTest.getResourcePath("test-comments.yml"));
        final String content = "#####################\n" +
            "## INITIAL COMMENT ##\n" +
            "#####################\n" +
            '\n' +
            "# Test comments\n" +
            "test:\n" +
            "  number: 5\n" +
            "  # Hello!\n" +
            "  string: Hello world\n" +
            "  boolean: true\n" +
            "  # List of words\n" +
            "  list:\n" +
            "  - Each\n" +
            "  - word\n" +
            "  - will\n" +
            "  - be\n" +
            "  - in\n" +
            "  - a\n" +
            "  - separated\n" +
            "    # Comment on a list item\n" +
            "  - entry # :)\n" +
            '\n' +
            "# Wonderful number\n" +
            "math:\n" +
            "  pi: 3.141592653589793\n" +
            "  # Comment without direct key\n" +
            '\n' +
            "# Some timestamps\n" +
            "timestamp:\n" +
            "  # ISO\n" +
            "  canonicalDate: 2020-07-04T13:18:04.458Z\n" +
            "  # Date/Time with format\n" +
            "  formattedDate: 04/07/2020 15:18:04\n" +
            '\n' +
            "# End\n";
        yamlFile.createOrLoadWithComments();
        MatcherAssert.assertThat(
            "Couldn't load the file with comments!",
            yamlFile.saveToStringWithComments(),
            new IsEqual<>(content)
        );
    }

    @Test
    void save() throws Exception {
        final YamlFile yamlFile = new YamlFile(YamlFileTest.getResourcePath("test-saved-comments.yml"));
        yamlFile.createOrLoad();
        yamlFile.save();
        yamlFile.save(new File(YamlFileTest.getResourcePath("test-saved-comments.yml")));
        yamlFile.save(YamlFileTest.getResourcePath("test-saved-comments.yml"));
    }

    @Test
    void saveWithComments() throws Exception {
        final YamlFile yamlFile = new YamlFile(YamlFileTest.getResourcePath("test-saved-comments.yml"));
        yamlFile.createOrLoad();
        yamlFile.saveWithComments();
    }

    @Test
    void fileToString() throws Exception {
        final YamlFile yamlFile = new YamlFile(YamlFileTest.getResourcePath("test.yml"));
        final String linuxcontent = "test:\n" +
            "  number: 5\n" +
            "  string: Hello world\n" +
            "  boolean: true\n" +
            "  list:\n" +
            "    - Each\n" +
            "    - word\n" +
            "    - will\n" +
            "    - be\n" +
            "    - in\n" +
            "    - a\n" +
            "    - separated\n" +
            "    - entry\n" +
            "math:\n" +
            "  pi: 3.141592653589793\n" +
            "timestamp:\n" +
            "  canonicalDate: 2020-07-04T13:18:04.458Z\n" +
            "  formattedDate: 04/07/2020 15:18:04\n";
        final String windowscontent = "test:\r\n" +
            "  number: 5\r\n" +
            "  string: Hello world\r\n" +
            "  boolean: true\r\n" +
            "  list:\r\n" +
            "    - Each\r\n" +
            "    - word\r\n" +
            "    - will\r\n" +
            "    - be\r\n" +
            "    - in\r\n" +
            "    - a\r\n" +
            "    - separated\r\n" +
            "    - entry\r\n" +
            "math:\r\n" +
            "  pi: 3.141592653589793\r\n" +
            "timestamp:\r\n" +
            "  canonicalDate: 2020-07-04T13:18:04.458Z\r\n" +
            "  formattedDate: 04/07/2020 15:18:04\r\n";
        final String content;
        if (System.getProperty("os.name").contains("Windows")) {
            content = windowscontent;
        } else {
            content = linuxcontent;
        }
        MatcherAssert.assertThat(
            "Couldn't get the content of the file (fileToString)!",
            yamlFile.fileToString(),
            new IsEqual<>(content)
        );

        yamlFile.load();
        yamlFile.set("test.number", 10);

        MatcherAssert.assertThat(
            "fileToString must not change until save!",
            yamlFile.fileToString(),
            new IsEqual<>(content)
        );

        final String newContent = "test:\n" +
            "  number: 10\n" +
            "  string: Hello world\n" +
            "  boolean: true\n" +
            "  list:\n" +
            "  - Each\n" +
            "  - word\n" +
            "  - will\n" +
            "  - be\n" +
            "  - in\n" +
            "  - a\n" +
            "  - separated\n" +
            "  - entry\n" +
            "math:\n" +
            "  pi: 3.141592653589793\n" +
            "timestamp:\n" +
            "  canonicalDate: 2020-07-04T13:18:04.458Z\n" +
            "  formattedDate: 04/07/2020 15:18:04\n";

        yamlFile.setConfigurationFile(new TempFile().value().toFile());
        yamlFile.save();

        MatcherAssert.assertThat(
            "Couldn't get the content of the file after save (fileToString)!",
            yamlFile.fileToString(),
            new IsEqual<>(newContent)
        );
    }

    @Test
    void saveToString() throws Exception {
        final YamlFile yamlFile = new YamlFile(YamlFileTest.getResourcePath("test.yml"));
        yamlFile.load();
        final String content = "test:\n" +
            "  number: 5\n" +
            "  string: Hello world\n" +
            "  boolean: true\n" +
            "  list:\n" +
            "  - Each\n" +
            "  - word\n" +
            "  - will\n" +
            "  - be\n" +
            "  - in\n" +
            "  - a\n" +
            "  - separated\n" +
            "  - entry\n" +
            "math:\n" +
            "  pi: 3.141592653589793\n" +
            "timestamp:\n" +
            "  canonicalDate: 2020-07-04T13:18:04.458Z\n" +
            "  formattedDate: 04/07/2020 15:18:04\n";

        MatcherAssert.assertThat(
            "Couldn't get the content of the file (saveToString)!",
            yamlFile.saveToString(),
            new IsEqual<>(content));

        MatcherAssert.assertThat(
            "Couldn't get the content of the file (toString)!",
            yamlFile.toString(),
            new IsEqual<>(content));
    }

    @Test
    void saveToStringWithComments() throws Exception {
        final YamlFile yamlFile = new YamlFile(YamlFileTest.getResourcePath("test-comments.yml"));
        yamlFile.loadWithComments();

        final String content = "#####################\n" +
            "## INITIAL COMMENT ##\n" +
            "#####################\n" +
            '\n' +
            "# Test comments\n" +
            "test:\n" +
            "  number: 5\n" +
            "  # Hello!\n" +
            "  string: Hello world\n" +
            "  boolean: true\n" +
            "  # List of words\n" +
            "  list:\n" +
            "  - Each\n" +
            "  - word\n" +
            "  - will\n" +
            "  - be\n" +
            "  - in\n" +
            "  - a\n" +
            "  - separated\n" +
            "    # Comment on a list item\n" +
            "  - entry # :)\n" +
            '\n' +
            "# Wonderful number\n" +
            "math:\n" +
            "  pi: 3.141592653589793\n" +
            "  # Comment without direct key\n" +
            '\n' +
            "# Some timestamps\n" +
            "timestamp:\n" +
            "  # ISO\n" +
            "  canonicalDate: 2020-07-04T13:18:04.458Z\n" +
            "  # Date/Time with format\n" +
            "  formattedDate: 04/07/2020 15:18:04\n" +
            '\n' +
            "# End\n";

        MatcherAssert.assertThat(
            "Couldn't get the content of the file (saveToStringWithComments)!",
            yamlFile.saveToStringWithComments(),
            new IsEqual<>(content));

        MatcherAssert.assertThat(
            "Couldn't get the content of the file (toString)!",
            yamlFile.toString(),
            new IsEqual<>(content));
    }

    @Test
    void saveToStringWithComments2() throws Exception {
        final YamlFile yamlFile = new YamlFile(YamlFileTest.getResourcePath("test-comments2.yml"));
        yamlFile.loadWithComments();

        final String content = "# Section\n" +
            "section:\n" +
            "  # Sub section\n" +
            "  sub-section-1:\n" +
            "    # List of numbers\n" +
            "    list:\n" +
            "    - 1\n" +
            "    - 2\n" +
            "    - 3\n" +
            "  sub-section-2: # Side comment\n" +
            "    list:\n" +
            "    - 1\n" +
            "    - 2 # Side comment on an arbitrary element\n" +
            "    - 3\n" +
            "  sub-section-3:\n" +
            "    # List of numbers\n" +
            "    list:        # Side comment with extra space\n" +
            "    - 1\n" +
            "    - 2\n" +
            "    - 3\n";

        MatcherAssert.assertThat(
            "Couldn't get the content of the file (saveToStringWithComments)!",
            yamlFile.saveToStringWithComments(),
            new IsEqual<>(content));

        MatcherAssert.assertThat(
            "Couldn't get the content of the file (toString)!",
            yamlFile.toString(),
            new IsEqual<>(content));
    }

    @Test
    void saveToStringWithComments3() throws Exception {
        final YamlFile yamlFile = new YamlFile(YamlFileTest.getResourcePath("test-comments3.yml"));
        yamlFile.loadWithComments();

        final String content = "backup-config:\n" +
            '\n' +
            "  #######################################################################################################################\n" +
            "  # SERVER-FILES BACKUP\n" +
            "  #######################################################################################################################\n" +
            '\n' +
            "  # Backups your server.jar and all setting files before startup to /backups/server/...zip\n" +
            "  server-files-backup:\n" +
            "    enable: false\n" +
            '\n' +
            "    # Set max-days to 0 if you want to keep your server backups forever.\n" +
            "    max-days: 7\n" +
            '\n' +
            '\n' +
            "  #######################################################################################################################\n" +
            "  # WORLDS BACKUP\n" +
            "  #######################################################################################################################\n" +
            '\n' +
            "  # Backups all folders starting with \"world\" to /backups/worlds/...zip\n" +
            "  worlds-backup:\n" +
            "    enable: false\n" +
            '\n' +
            "    # Set max-days to 0 if you want to keep your world backups forever.\n" +
            "    max-days: 7\n" +
            '\n' +
            '\n' +
            "  #######################################################################################################################\n" +
            "  # PLUGINS BACKUP\n" +
            "  #######################################################################################################################\n" +
            '\n' +
            "  # Backups your plugins folder before startup to /backups/plugins/...zip\n" +
            "  plugins-backup:\n" +
            "    enable: true\n" +
            '\n' +
            "    # Set max-days to 0 if you want to keep your plugin backups forever.\n" +
            "    max-days: 7\n" +
            '\n';

        MatcherAssert.assertThat(
            "Couldn't get the content of the file (saveToStringWithComments)!",
            yamlFile.saveToStringWithComments(),
            new IsEqual<>(content));

        MatcherAssert.assertThat(
            "Couldn't get the content of the file (toString)!",
            yamlFile.toString(),
            new IsEqual<>(content));
    }

    @Test
    void getComment() throws Exception {
        final YamlFile yamlFile = new YamlFile(YamlFileTest.getResourcePath("test-comments.yml"));
        yamlFile.loadWithComments();

        MatcherAssert.assertThat(
            "Couldn't parse the comments correctly!",
            yamlFile.getComment("test.string"),
            new IsEqual<>("Hello!")
        );
    }

    @Test
    void setComment() throws Exception {
        final YamlFile yamlFile = new YamlFile(YamlFileTest.getResourcePath("test-comments.yml"));
        yamlFile.loadWithComments();
        yamlFile.setComment("test.string", "Edited hello comment!");
        yamlFile.setComment("test.string", "Edited hello side comment!", CommentType.SIDE);
        MatcherAssert.assertThat(
            "Couldn't parse the comments correctly!",
            yamlFile.getComment("test.string"),
            new IsEqual<>("Edited hello comment!")
        );
        MatcherAssert.assertThat(
            "Couldn't parse the comments correctly!",
            yamlFile.getComment("test.string", CommentType.SIDE),
            new IsEqual<>("Edited hello side comment!")
        );
    }

    @Test
    void exists() throws Exception {
        final YamlFile yamlFile = new YamlFile(YamlFileTest.getResourcePath("test-comments.yml"));
        yamlFile.createOrLoadWithComments();

        MatcherAssert.assertThat(
            "The file couldn't found!",
            yamlFile.exists(),
            new IsTrue()
        );
    }

    @Test
    void createNewFile() throws Exception {
        final YamlFile yamlFile = new YamlFile(YamlFileTest.getResourcePath("test-comments.yml"));
        yamlFile.createNewFile(false);
        MatcherAssert.assertThat(
            "The file couldn't found!",
            yamlFile.exists(),
            new IsTrue()
        );
    }

    @Test
    void deleteFile() throws Exception {
        final YamlFile yamlFile = new YamlFile(YamlFileTest.getResourcePath("test-deleted-comments.yml"));
        yamlFile.createOrLoadWithComments();
        yamlFile.deleteFile();

        MatcherAssert.assertThat(
            "The file found!",
            yamlFile.exists(),
            new IsNot<>(new IsTrue())
        );
    }

    @Test
    void getSize() throws Exception {
        final YamlFile yamlFile = new YamlFile(YamlFileTest.getResourcePath("test-comments.yml"));
        yamlFile.loadWithComments();
        final String content = "# ####################\n" +
            "# # INITIAL COMMENT ##\n" +
            "# ####################\n" +
            "# \n" +
            "# Test comments\n" +
            "test:\n" +
            "  number: 5\n" +
            "  # Hello!\n" +
            "  string: Hello world\n" +
            "  boolean: true\n" +
            "  # List of words\n" +
            "  list:\n" +
            "    - Each\n" +
            "    - word\n" +
            "    - will\n" +
            "    - be\n" +
            "    - in\n" +
            "    - a\n" +
            "    - separated\n" +
            "    # Comment on a list item\n" +
            "    - entry # :)\n" +
            '\n' +
            "# Wonderful number\n" +
            "math:\n" +
            "  pi: 3.141592653589793\n" +
            "  # Comment without direct key\n" +
            '\n' +
            "# Some timestamps\n" +
            "timestamp:\n" +
            "  # ISO\n" +
            "  canonicalDate: 2020-07-04T13:18:04.458Z\n" +
            "  # Date/Time with format\n" +
            "  formattedDate: 04/07/2020 15:18:04\n" +
            '\n' +
            "# End\n";

//        MatcherAssert.assertThat(
//            "The file size is not correct!",
//            yamlFile.getSize(),
//            new IsEqual<>(((long) content.getBytes().length))
//        );
    }

    @Test
    void getFilePath() {
        final File file = new File(YamlFileTest.getResourcePath("test.yml"));
        final YamlFile yamlFile = new YamlFile(file);

        MatcherAssert.assertThat(
            "Configuration file is not the same!",
            yamlFile.getFilePath(),
            new IsEqual<>(file.getAbsolutePath()));
    }

    @Test
    void getConfigurationFile() {
        final File file = new File(YamlFileTest.getResourcePath("test.yml"));
        final YamlFile yamlFile = new YamlFile(file);

        MatcherAssert.assertThat(
            "Configuration file is not the same!",
            yamlFile.getConfigurationFile(),
            new IsEqual<>(file));
    }

    @Test
    void setConfigurationFile() {
        final File file = new File(YamlFileTest.getResourcePath("test.yml"));
        final YamlFile yamlFile = new YamlFile();

        yamlFile.setConfigurationFile(file);

        MatcherAssert.assertThat(
            "Configuration file has not changed!",
            yamlFile.getConfigurationFile(),
            new IsEqual<>(file));
    }

    @Test
    void copyTo() throws Exception {
        final YamlFile yamlFile = new YamlFile(YamlFileTest.getResourcePath("test-comments.yml"));
        yamlFile.loadWithComments();
        final String resourcePath = YamlFileTest.getResourcePath("test-copy-to.yml");
        final File copyto = new File(resourcePath);
        copyto.createNewFile();
        yamlFile.copyTo(copyto);
        yamlFile.copyTo(resourcePath);

        final YamlFile copied = new YamlFile(YamlFileTest.getResourcePath("test-copy-to.yml"));
        copied.loadWithComments();
        final String content = "#####################\n" +
            "## INITIAL COMMENT ##\n" +
            "#####################\n" +
            '\n' +
            "# Test comments\n" +
            "test:\n" +
            "  number: 5\n" +
            "  # Hello!\n" +
            "  string: Hello world\n" +
            "  boolean: true\n" +
            "  # List of words\n" +
            "  list:\n" +
            "    - Each\n" +
            "    - word\n" +
            "    - will\n" +
            "    - be\n" +
            "    - in\n" +
            "    - a\n" +
            "    - separated\n" +
            "    # Comment on a list item\n" +
            "    - entry # :)\n" +
            '\n' +
            "# Wonderful number\n" +
            "math:\n" +
            "  pi: 3.141592653589793\n" +
            "  # Comment without direct key\n" +
            '\n' +
            "# Some timestamps\n" +
            "timestamp:\n" +
            "  # ISO\n" +
            "  canonicalDate: 2020-07-04T13:18:04.458Z\n" +
            "  # Date/Time with format\n" +
            "  formattedDate: 04/07/2020 15:18:04\n" +
            '\n' +
            "# End\n";
        MatcherAssert.assertThat(
            "Couldn't copied the file!",
            copied.saveToStringWithComments(),
            new IsEqual<>(content)
        );
    }

}
