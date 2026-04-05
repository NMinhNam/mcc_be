# AGENTS.md - Development Guidelines for erp_bom

## Project Overview

This is a Spring Boot 4.0.3 application with Java 17, using Maven as the build tool. The project is an ERP system (BOM management) using MyBatis Plus with PostgreSQL.

## Build Commands

```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run

# Build without running tests
mvn clean install -DskipTests

# Run a specific test class
mvn test -Dtest=ProductionOrderServiceTest

# Run a specific test method
mvn test -Dtest=ProductionOrderServiceTest#testCreate

# Package as JAR
mvn package
```

## Project Structure

```
src/main/java/com/erp/bom/
├── ErpBomApplication.java          # Main entry point
├── config/                         # Configuration classes
├── feature/                       # Feature modules (package-by-feature)
│   ├── auth/
│   │   ├── controller/
│   │   ├── service/
│   │   ├── mapper/
│   │   ├── entity/
│   │   └── dto/
│   ├── production/
│   │   ├── controller/
│   │   ├── service/
│   │   │   └── impl/
│   │   ├── mapper/
│   │   └── entity/
│   ├── product/
│   │   ├── controller/
│   │   ├── service/
│   │   ├── mapper/
│   │   ├── entity/
│   │   └── dto/
│   └── material/
│       ├── controller/
│       ├── service/
│       ├── mapper/
│       └── entity/
└── utils/
    └── excel/
```

## Code Style Guidelines

### Naming Conventions

- **Classes**: PascalCase (e.g., `ProductionOrderService`, `ProductionOrder`)
- **Methods**: camelCase (e.g., `getById`, `createProductionOrder`)
- **Variables**: camelCase (e.g., `productionOrderMapper`, `orderNo`)
- **Constants**: UPPER_SNAKE_CASE (e.g., `MAX_FILE_SIZE`, `EXCEL_CONTENT_TYPE`)
- **Enums**: lowercase with underscores (e.g., `draft`, `in_progress`, `done`)
- **Packages**: lowercase (e.g., `com.erp.bom.feature.production`)

### Import Organization

Organize imports in the following order:
1. Java/Jakarta EE imports
2. Spring Framework imports
3. Third-party library imports (MyBatis Plus, Lombok, etc.)
4. Internal package imports

```java
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.erp.bom.feature.production.entity.ProductionOrder;
```

### Constructor Injection (Required)

Always use constructor injection with `final` fields. Do not use field injection.

```java
@Service
public class ProductionOrderService {

    private final ProductionOrderMapper productionOrderMapper;

    public ProductionOrderService(ProductionOrderMapper productionOrderMapper) {
        this.productionOrderMapper = productionOrderMapper;
    }
}
```

### Controller Guidelines

- Use `@RestController` and `@RequestMapping` for REST endpoints
- Always inject service via constructor
- Use SLF4J for logging with the class-level logger
- Log incoming requests and responses for debugging

```java
@RestController
@RequestMapping("/api/production-orders")
public class ProductionOrderController {

    private static final Logger log = LoggerFactory.getLogger(ProductionOrderController.class);

    private final ProductionOrderService productionOrderService;

    public ProductionOrderController(ProductionOrderService productionOrderService) {
        this.productionOrderService = productionOrderService;
    }

    @PostMapping
    public ResponseEntity<ProductionOrder> create(@RequestBody ProductionOrder productionOrder) {
        log.info("[POST /api/production-orders] Payload: {}", productionOrder);
        // ...
    }
}
```

### Service Layer

- Business logic goes in service layer
- Service implementations go in `impl/` subpackage
- Use appropriate HTTP status codes (201 for created, 404 for not found, etc.)
- Handle exceptions with meaningful messages

```java
public ProductionOrder update(Long id, ProductionOrder productionOrder) {
    ProductionOrder existing = productionOrderMapper.selectById(id);
    if (existing == null) {
        throw new RuntimeException("ProductionOrder not found with id: " + id);
    }
    // ...
}
```

### Error Handling

- Use custom exceptions for domain-specific errors
- Include meaningful error messages with entity identifiers
- Return appropriate HTTP status codes
- Consider global exception handling with `@ControllerAdvice`

### Entity/Database Layer

- Use MyBatis Plus for database operations
- Entity classes should follow database table naming (snake_case in DB, camelCase in Java)
- Use `@TableName` annotation if table name differs from entity name
- Use enums for fixed status values

```java
public enum ProductionOrderStatus {
    draft,
    confirmed,
    in_progress,
    done,
    cancelled
}
```

### Utility Classes

- Utility classes should have a private constructor to prevent instantiation
- Use `public static final` for constants
- Keep methods focused and single-purpose

```java
public class ExcelUtil {

    private ExcelUtil() {}

    public static final String EXCEL_CONTENT_TYPE = "application/vnd.openxmlformats...";
    // ...
}
```

### DTOs (Data Transfer Objects)

- Create separate DTOs for requests and responses when needed
- Use naming convention: `{EntityName}Request`, `{EntityName}Response`
- Keep DTOs in the `dto` subpackage within each feature

### Lombok Usage

- Use `@Data` sparingly; prefer `@Getter`/`@Setter` when only specific methods are needed
- Use `@Builder` for complex objects
- Use `@AllArgsConstructor` and `@NoArgsConstructor` as needed
- Always use `@TableField` when database column names differ from field names

### REST API Conventions

- Use plural nouns for resources: `/api/production-orders` (not `/api/production-order`)
- Use proper HTTP methods: GET (read), POST (create), PUT (update), DELETE (remove)
- Use path variables for resource identifiers: `/api/production-orders/{id}`
- Use query parameters for filtering: `/api/production-orders?status=pending`

### Documentation

- Add Javadoc for public API methods
- Document expected input/output formats
- Use OpenAPI/Swagger annotations for API documentation (`@Operation`, `@ApiParam`, etc.)
- Include meaningful comments for complex business logic

### Testing

- Write unit tests for service layer
- Use `@SpringBootTest` for integration tests
- Mock external dependencies
- Test edge cases and error scenarios

### Database

- PostgreSQL is used as the database
- Use MyBatis Plus QueryWrapper for dynamic queries
- Always use parameterized queries to prevent SQL injection

### API Response Patterns

```java
// Single resource
ResponseEntity<ProductionOrder> or ResponseEntity.notFound().build()

// Collections
ResponseEntity<List<ProductionOrder>> or ResponseEntity.ok(list)

// Pagination
ResponseEntity<Page<ProductionOrder>>
```

### Security

- JWT authentication is implemented
- Use `@PreAuthorize` for role-based access control when needed
- Never expose sensitive data in API responses

## Key Dependencies

- Spring Boot 4.0.3
- MyBatis Plus 3.5.5
- PostgreSQL
- Lombok
- EasyExcel (Alibaba) for Excel operations
- SpringDoc OpenAPI for API documentation
- Cloudinary for image storage
- P6Spy for SQL logging
