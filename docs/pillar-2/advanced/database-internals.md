# Database Internals & Storage Engines

## Learning Objectives
- Understand B+ Trees and LSM Trees
- Compare different storage engine architectures
- Apply database internals knowledge to system design

## Key Topics
- **B+Trees**: OLTP workloads, MySQL InnoDB
- **LSM Trees**: Write-heavy workloads, Cassandra, RocksDB
- **Indexing Strategies**: Primary, secondary, composite indexes
- **ACID Properties**: Atomicity, Consistency, Isolation, Durability

## B+Trees
A B-tree of order K is a tree structure with the following properties:

- Each node in the tree stores N key/value pairs, where N is greater than 1 and less than or equal to K.
- Each internal node has at least N/2 key/value pairs (an internal node is one that is not a leaf or the root).
- Each node has N+1 children.
- The root node has at least one value and two children, unless it is the sole node.
- All leaves are on the same level.


> B-trees are uniquely suited to work well when you have a very large quantity of data that also needs to be persisted to long-term storage (disk). This is because each node uses a fixed number of bytes. The number of bytes can be tailored to play nicely with **disk blocks**.  
> Reading and writing data on hard-drive disks (HDDs) and solid-state disks (SSDs) is done in units called **blocks**. These are typically byte sequences of length 4096, 8192, or 16384 (4k, 8k, 16k). A single disk will have a capacity of many millions or billions of blocks. RAM on the other hand is typically addressable on a per-byte level.  
> This is why B-trees work so well when we need to organize and store persistent data on disk. Each node of a B-tree can be sized to match the size of a disk block (or a multiple of this size).[^1]

[^1]: https://planetscale.com/blog/btrees-and-database-indexes


## Advanced Concepts
- Write-Ahead Logging (WAL)
- Multi-Version Concurrency Control (MVCC)
- Database sharding and partitioning strategies

*Content to be expanded...*
