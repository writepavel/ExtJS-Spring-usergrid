package com.writepavel.usergrid.config;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.embedded.*;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.util.Assert;

/**
 * Created by Pavel on 07.08.2014.
 * This is copy of class org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder,
 * adapted for using DerbyInFileEmbeddedDatabaseConfigurer.
 */
public class DerbyInFileEmbeddedDatabaseBuilder {

    private final EmbeddedDatabaseFactory databaseFactory;

    private final ResourceDatabasePopulator databasePopulator;

    private final ResourceLoader resourceLoader;


    /**
     * Create a new embedded database builder with a {@link org.springframework.core.io.DefaultResourceLoader}.
     */
    public DerbyInFileEmbeddedDatabaseBuilder() {
        this(new DefaultResourceLoader());
    }

    /**
     * Create a new embedded database builder with the given {@link ResourceLoader}.
     * @param resourceLoader the {@code ResourceLoader} to delegate to
     */
    public DerbyInFileEmbeddedDatabaseBuilder(ResourceLoader resourceLoader) {
        this.databaseFactory = new EmbeddedDatabaseFactory();
        try {
            this.databaseFactory.setDatabaseConfigurer(DerbyInFileEmbeddedDatabaseConfigurer.getInstance());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        this.databasePopulator = new ResourceDatabasePopulator();
        this.databaseFactory.setDatabasePopulator(this.databasePopulator);
        this.resourceLoader = resourceLoader;
    }

    /**
     * Set the name of the embedded database.
     * <p>Defaults to {@link EmbeddedDatabaseFactory#DEFAULT_DATABASE_NAME} if
     * not called.
     * @param databaseName the name of the embedded database to build
     * @return {@code this}, to facilitate method chaining
     */
    public DerbyInFileEmbeddedDatabaseBuilder setName(String databaseName) {
        this.databaseFactory.setDatabaseName(databaseName);
        return this;
    }

//    /**
//     * Set the type of embedded database.
//     * <p>Defaults to HSQL if not called.
//     * @param databaseType the type of embedded database to build
//     * @return {@code this}, to facilitate method chaining
//     */
//    public DerbyInFileEmbeddedDatabaseBuilder setType(EmbeddedDatabaseType databaseType) {
//        this.databaseFactory.setDatabaseType(databaseType);
//        return this;
//    }

    /**
     * Set the factory to use to create the {@link javax.sql.DataSource} instance that
     * connects to the embedded database.
     * <p>Defaults to {@link org.springframework.jdbc.datasource.embedded.SimpleDriverDataSourceFactory} but can be overridden,
     * for example to introduce connection pooling.
     * @return {@code this}, to facilitate method chaining
     * @since 4.0.3
     */
    public DerbyInFileEmbeddedDatabaseBuilder setDataSourceFactory(DataSourceFactory dataSourceFactory) {
        Assert.notNull(dataSourceFactory, "DataSourceFactory is required");
        this.databaseFactory.setDataSourceFactory(dataSourceFactory);
        return this;
    }

    /**
     * Add default SQL scripts to execute to populate the database.
     * <p>The default scripts are {@code "schema.sql"} to create the database
     * schema and {@code "data.sql"} to populate the database with data.
     * @return {@code this}, to facilitate method chaining
     */
    public DerbyInFileEmbeddedDatabaseBuilder addDefaultScripts() {
        return addScripts("schema.sql", "data.sql");
    }

    /**
     * Add an SQL script to execute to initialize or populate the database.
     * @param script the script to execute
     * @return {@code this}, to facilitate method chaining
     */
    public DerbyInFileEmbeddedDatabaseBuilder addScript(String script) {
        this.databasePopulator.addScript(this.resourceLoader.getResource(script));
        return this;
    }

    /**
     * Add multiple SQL scripts to execute to initialize or populate the database.
     * @param scripts the scripts to execute
     * @return {@code this}, to facilitate method chaining
     * @since 4.0.3
     */
    public DerbyInFileEmbeddedDatabaseBuilder addScripts(String... scripts) {
        for (String script : scripts) {
            addScript(script);
        }
        return this;
    }

    /**
     * Specify the character encoding used in all SQL scripts, if different from
     * the platform encoding.
     * @param scriptEncoding the encoding used in scripts
     * @return {@code this}, to facilitate method chaining
     * @since 4.0.3
     */
    public DerbyInFileEmbeddedDatabaseBuilder setScriptEncoding(String scriptEncoding) {
        this.databasePopulator.setSqlScriptEncoding(scriptEncoding);
        return this;
    }

    /**
     * Specify the statement separator used in all SQL scripts, if a custom one.
     * <p>Defaults to {@code ";"} if not specified and falls back to {@code "\n"}
     * as a last resort; may be set to {@link org.springframework.jdbc.datasource.init.ScriptUtils#EOF_STATEMENT_SEPARATOR}
     * to signal that each script contains a single statement without a separator.
     * @param separator the statement separator
     * @return {@code this}, to facilitate method chaining
     * @since 4.0.3
     */
    public DerbyInFileEmbeddedDatabaseBuilder setSeparator(String separator) {
        this.databasePopulator.setSeparator(separator);
        return this;
    }

    /**
     * Specify the single-line comment prefix used in all SQL scripts.
     * <p>Defaults to {@code "--"}.
     * @param commentPrefix the prefix for single-line comments
     * @return {@code this}, to facilitate method chaining
     * @since 4.0.3
     */
    public DerbyInFileEmbeddedDatabaseBuilder setCommentPrefix(String commentPrefix) {
        this.databasePopulator.setCommentPrefix(commentPrefix);
        return this;
    }

    /**
     * Specify the start delimiter for block comments in all SQL scripts.
     * <p>Defaults to {@code "/*"}.
     * @param blockCommentStartDelimiter the start delimiter for block comments
     * @return {@code this}, to facilitate method chaining
     * @since 4.0.3
     * @see #setBlockCommentEndDelimiter
     * @since 4.0.3
     */
    public DerbyInFileEmbeddedDatabaseBuilder setBlockCommentStartDelimiter(String blockCommentStartDelimiter) {
        this.databasePopulator.setBlockCommentStartDelimiter(blockCommentStartDelimiter);
        return this;
    }

    /**
     * Specify the end delimiter for block comments in all SQL scripts.
     * <p>Defaults to <code>"*&#47;"</code>.
     * @param blockCommentEndDelimiter the end delimiter for block comments
     * @return {@code this}, to facilitate method chaining
     * @since 4.0.3
     * @see #setBlockCommentStartDelimiter
     * @since 4.0.3
     */
    public DerbyInFileEmbeddedDatabaseBuilder setBlockCommentEndDelimiter(String blockCommentEndDelimiter) {
        this.databasePopulator.setBlockCommentEndDelimiter(blockCommentEndDelimiter);
        return this;
    }

    /**
     * Specify that all failures which occur while executing SQL scripts should
     * be logged but should not cause a failure.
     * <p>Defaults to {@code false}.
     * @param flag {@code true} if script execution should continue on error
     * @return {@code this}, to facilitate method chaining
     * @since 4.0.3
     */
    public DerbyInFileEmbeddedDatabaseBuilder continueOnError(boolean flag) {
        this.databasePopulator.setContinueOnError(flag);
        return this;
    }

    /**
     * Specify that a failed SQL {@code DROP} statement within an executed
     * script can be ignored.
     * <p>This is useful for a database whose SQL dialect does not support an
     * {@code IF EXISTS} clause in a {@code DROP} statement.
     * <p>The default is {@code false} so that {@link #build building} will fail
     * fast if a script starts with a {@code DROP} statement.
     * @param flag {@code true} if failed drop statements should be ignored
     * @return {@code this}, to facilitate method chaining
     * @since 4.0.3
     */
    public DerbyInFileEmbeddedDatabaseBuilder ignoreFailedDrops(boolean flag) {
        this.databasePopulator.setIgnoreFailedDrops(flag);
        return this;
    }

    /**
     * Build the embedded database.
     * @return the embedded database
     */
    public EmbeddedDatabase build() {
        return this.databaseFactory.getDatabase();
    }

}

