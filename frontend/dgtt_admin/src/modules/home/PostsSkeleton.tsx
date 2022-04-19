import React from "react";
import { Skeleton, Card } from "antd";


const PostsSkeleton = () => {
  return (
    <div>
      <Card bordered={false} className="mb-2 ml-4 mr-4 mr-md-0">
        <Skeleton active avatar paragraph={{ rows: 2 }} />
      </Card>
      <Card bordered={false} className="mb-2 ml-4 mr-4 mr-md-0">
        <Skeleton active avatar paragraph={{ rows: 2 }} />
      </Card>
      <Card bordered={false} className="mb-2 ml-4 mr-4 mr-md-0">
        <Skeleton active avatar paragraph={{ rows: 2 }} />
      </Card>
      <Card bordered={false} className="mb-2 ml-4 mr-4 mr-md-0">
        <Skeleton active avatar paragraph={{ rows: 2 }} />
      </Card>
      <Card bordered={false} className="mb-2 ml-4 mr-4 mr-md-0">
        <Skeleton active avatar paragraph={{ rows: 2 }} />
      </Card>
      <Card bordered={false} className="mb-2 ml-4 mr-4 mr-md-0">
        <Skeleton active avatar paragraph={{ rows: 2 }} />
      </Card>
    </div>
  );
};

export default PostsSkeleton;
